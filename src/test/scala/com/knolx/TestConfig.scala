package com.knolx

import com.knolx.traitdef.ColorDef
import org.apache.calcite.plan.volcano.VolcanoPlanner
import org.apache.calcite.plan.{RelOptCluster, RelOptPlanner}
import org.apache.calcite.rel.`type`.{RelDataTypeFactoryImpl, RelDataTypeSystem}
import org.apache.calcite.rex.RexBuilder
import org.apache.calcite.sql.`type`.SqlTypeFactoryImpl

class TestConfig {

  val cluster: RelOptCluster = relOptCluster

  private def relOptCluster: RelOptCluster =
    RelOptCluster.create(volcanoPlanner(), rexBuilder)

  private def volcanoPlanner(): RelOptPlanner = {
    val planner = new VolcanoPlanner()
    planner.addRelTraitDef(ColorDef)
    planner
  }

  private def sqlTypeFactory: RelDataTypeFactoryImpl = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT)

  private def rexBuilder: RexBuilder = new RexBuilder(sqlTypeFactory)
}

