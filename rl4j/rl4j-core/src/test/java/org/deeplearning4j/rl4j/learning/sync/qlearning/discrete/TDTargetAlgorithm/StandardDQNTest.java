package org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm;

import org.deeplearning4j.rl4j.learning.sync.Transition;
import org.deeplearning4j.rl4j.network.IOutputNeuralNet;
import org.deeplearning4j.rl4j.observation.Observation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StandardDQNTest {

    @Mock
    IOutputNeuralNet qNetworkMock;

    @Mock
    IOutputNeuralNet targetQNetworkMock;


    @Before
    public void setup() {
        when(qNetworkMock.output(any(INDArray.class))).thenAnswer(i -> i.getArguments()[0]);
        when(targetQNetworkMock.output(any(INDArray.class))).thenAnswer(i -> i.getArguments()[0]);
    }


    @Test
    public void when_isTerminal_expect_rewardValueAtIdx0() {

        // Assemble
        List<Transition<Integer>> transitions = new ArrayList<Transition<Integer>>() {
            {
                add(buildTransition(buildObservation(new double[]{1.1, 2.2}),
                        0, 1.0, true, buildObservation(new double[]{11.0, 22.0})));
            }
        };

        StandardDQN sut = new StandardDQN(qNetworkMock, targetQNetworkMock, 0.5);

        // Act
        DataSet result = sut.compute(transitions);

        // Assert
        INDArray evaluatedQValues = result.getLabels();
        assertEquals(1.0, evaluatedQValues.getDouble(0, 0), 0.0001);
        assertEquals(2.2, evaluatedQValues.getDouble(0, 1), 0.0001);
    }

    @Test
    public void when_isNotTerminal_expect_rewardPlusEstimatedQValue() {

        // Assemble
        List<Transition<Integer>> transitions = new ArrayList<Transition<Integer>>() {
            {
                add(buildTransition(buildObservation(new double[]{1.1, 2.2}),
                        0, 1.0, false, buildObservation(new double[]{11.0, 22.0})));
            }
        };

        StandardDQN sut = new StandardDQN(qNetworkMock, targetQNetworkMock, 0.5);

        // Act
        DataSet result = sut.compute(transitions);

        // Assert
        INDArray evaluatedQValues = result.getLabels();
        assertEquals(1.0 + 0.5 * 22.0, evaluatedQValues.getDouble(0, 0), 0.0001);
        assertEquals(2.2, evaluatedQValues.getDouble(0, 1), 0.0001);
    }

    @Test
    public void when_batchHasMoreThanOne_expect_everySampleEvaluated() {

        // Assemble
        List<Transition<Integer>> transitions = new ArrayList<Transition<Integer>>() {
            {
                add(buildTransition(buildObservation(new double[]{1.1, 2.2}),
                        0, 1.0, false, buildObservation(new double[]{11.0, 22.0})));
                add(buildTransition(buildObservation(new double[]{3.3, 4.4}),
                        1, 2.0, false, buildObservation(new double[]{33.0, 44.0})));
                add(buildTransition(buildObservation(new double[]{5.5, 6.6}),
                        0, 3.0, true, buildObservation(new double[]{55.0, 66.0})));
            }
        };

        StandardDQN sut = new StandardDQN(qNetworkMock, targetQNetworkMock, 0.5);

        // Act
        DataSet result = sut.compute(transitions);

        // Assert
        INDArray evaluatedQValues = result.getLabels();
        assertEquals((1.0 + 0.5 * 22.0), evaluatedQValues.getDouble(0, 0), 0.0001);
        assertEquals(2.2, evaluatedQValues.getDouble(0, 1), 0.0001);

        assertEquals(3.3, evaluatedQValues.getDouble(1, 0), 0.0001);
        assertEquals((2.0 + 0.5 * 44.0), evaluatedQValues.getDouble(1, 1), 0.0001);

        assertEquals(3.0, evaluatedQValues.getDouble(2, 0), 0.0001); // terminal: reward only
        assertEquals(6.6, evaluatedQValues.getDouble(2, 1), 0.0001);

    }

    private Observation buildObservation(double[] data) {
        return new Observation(Nd4j.create(data).reshape(1, 2));
    }

    private Transition<Integer> buildTransition(Observation observation, Integer action, double reward, boolean isTerminal, Observation nextObservation) {
        Transition<Integer> result = new Transition<Integer>(observation, action, reward, isTerminal);
        result.setNextObservation(nextObservation);

        return result;
    }
}
