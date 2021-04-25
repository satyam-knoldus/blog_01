package com.knolx.physical

import com.knolx.traitdef.ColorDef
import org.apache.calcite.jdbc.JavaTypeFactoryImpl
import org.apache.calcite.plan.{RelOptCluster, RelTraitSet}
import org.apache.calcite.rel.AbstractRelNode.sole
import org.apache.calcite.rel.`type`.RelDataType
import org.apache.calcite.rel.{AbstractRelNode, RelNode, RelWriter, SingleRel}

import java.util

sealed trait CustomRel extends AbstractRelNode {
  override def explainTerms(pw: RelWriter): RelWriter = {
    val writer = super.explainTerms(pw)
    writer.item("Color", getTraitSet.getTrait(ColorDef))
  }

  override def deriveRowType(): RelDataType = {
    new JavaTypeFactoryImpl().createUnknownType()
  }
}

class ColoredRel(val cluster: RelOptCluster, val traits: RelTraitSet, input: RelNode) extends SingleRel(cluster, traits, input) with CustomRel {
  override def copy(traitSet: RelTraitSet, inputs: util.List[RelNode]): RelNode = new ColoredRel(getCluster, traitSet, sole(inputs))

}

class LeafRel(val cluster: RelOptCluster, val traits: RelTraitSet) extends AbstractRelNode(cluster, traits) with CustomRel {

  override def copy(traitSet: RelTraitSet, inputs: util.List[RelNode]): RelNode =
    if (inputs.isEmpty) new LeafRel(getCluster, traitSet)
    else new ColoredRel(getCluster, traitSet, sole(inputs))

}