/*******************************************************************************
 * Copyright (c) 2015-2018 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

//
//  @author raver119@gmail.com
//


#ifndef LIBND4J_OP_ENUMS_H
#define LIBND4J_OP_ENUMS_H

#include <loops/legacy_ops.h>
#include <type_boilerplate.h>
#include <enum_boilerplate.h>

namespace nd4j {
    enum class TransformOps {
       BUILD_ENUMERATION(TRANSFORM_OPS)
    };

    enum class PairwiseOps {
       BUILD_ENUMERATION(PAIRWISE_TRANSFORM_OPS)
    };

    enum class ScalarOps {
        BUILD_ENUMERATION(SCALAR_OPS)
    };

    enum class ReduceOps {
        BUILD_ENUMERATION(REDUCE_OPS)
    };

    enum class Reduce3Ops {
        BUILD_ENUMERATION(REDUCE3_OPS)
    };

    enum class IndexReduceOps {
        BUILD_ENUMERATION(INDEX_REDUCE_OPS)
    };

    enum class BroadcastOps {
        BUILD_ENUMERATION(BROADCAST_OPS)
    };

    enum class VarianceOps {
        BUILD_ENUMERATION(SUMMARY_STATS_OPS)
    };
}

#endif