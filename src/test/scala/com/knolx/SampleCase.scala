package com.knolx

import com.knolx.`trait`.ColorTrait
import com.knolx.physical.{ColoredRel, LeafRel}
import org.apache.calcite.plan.RelTraitSet

object SampleCases {

  val redTraitSet: RelTraitSet = RelTraitSet.createEmpty().plus(ColorTrait.Red)
  val greenTraitSet: RelTraitSet = RelTraitSet.createEmpty().plus(ColorTrait.Green)
  val blueTraitSet: RelTraitSet = RelTraitSet.createEmpty().plus(ColorTrait.Blue)
  val none: RelTraitSet = RelTraitSet.createEmpty().plus(ColorTrait.None)

  /**
   * ColoredRel ( Red )
   * ColoredRel ( Blue)
   * ColoredRel ( Green)
   */
  val CASE_1: ColoredRel = {
    val cluster = new TestConfig().cluster
    new ColoredRel(cluster, redTraitSet,
      new ColoredRel(cluster, greenTraitSet,
        new LeafRel(cluster, blueTraitSet)))
  }

}
