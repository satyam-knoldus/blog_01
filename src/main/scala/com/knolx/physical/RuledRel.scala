package com.knolx.physical

import com.knolx.traitdef.ColorDef
import org.apache.calcite.plan.{RelOptCluster, RelTraitSet}
import org.apache.calcite.rel.AbstractRelNode.sole
import org.apache.calcite.rel.{RelNode, RelWriter, SingleRel}

import java.util

class RuledRel(val cluster: RelOptCluster, val traits: RelTraitSet, input: RelNode) extends SingleRel(cluster, traits, input) {
  override def copy(traitSet: RelTraitSet, inputs: util.List[RelNode]): RelNode = new RuledRel(getCluster, traitSet, sole(inputs))

  override def explainTerms(pw: RelWriter): RelWriter = {
    val writer = super.explainTerms(pw)
    writer.item("Color", getTraitSet.getTrait(ColorDef))
  }
}
