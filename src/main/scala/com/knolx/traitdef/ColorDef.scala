package com.knolx.traitdef

import com.knolx.`trait`.ColorTrait
import org.apache.calcite.plan.{RelOptPlanner, RelTraitDef}
import org.apache.calcite.rel.RelNode

object ColorDef extends RelTraitDef[ColorTrait] {
  override def getTraitClass: Class[ColorTrait] = classOf[ColorTrait]

  override def getSimpleName: String = s"ColorTraitDef"

  override def getDefault: ColorTrait = ColorTrait.None

  override def convert(planner: RelOptPlanner, rel: RelNode, toTrait: ColorTrait, allowInfiniteCostConverters: Boolean): RelNode = {
   //Not using this method
    rel
  }

  override def canConvert(planner: RelOptPlanner, fromTrait: ColorTrait, toTrait: ColorTrait): Boolean = {
    fromTrait.satisfies(toTrait)
  }
}
