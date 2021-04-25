package com.knolx

import com.knolx.HepPlannerSpec.hepPlanner
import com.knolx.rule.RuleForGreen
import org.apache.calcite.plan.hep.{HepPlanner, HepProgramBuilder}
import org.apache.calcite.plan.{RelOptPlanner, RelOptRule, RelOptUtil}
import org.apache.calcite.sql.{SqlExplainFormat, SqlExplainLevel}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


object HepPlannerSpec {
  def hepPlanner(rules: Set[RelOptRule]): RelOptPlanner = {
    val builder = new HepProgramBuilder()
    rules.foreach(builder.addRuleInstance)
    new HepPlanner(builder.build)
  }
}

class HepPlannerSpec extends AnyWordSpec with Matchers {
  "For a green node " should {
    "add a ruled rel as parent" in {
      val planner = hepPlanner(Set(RuleForGreen.PRECEDED_BY_RULE))
      planner.setRoot(SampleCases.CASE_1)
      val result = planner.findBestExp()
      println(RelOptUtil.dumpPlan("Result", result, SqlExplainFormat.TEXT, SqlExplainLevel.EXPPLAN_ATTRIBUTES))
    }
    "add a ruled rel as child" in {
      val planner = hepPlanner(Set(RuleForGreen.SUCCEEDED_BY_RULE))
      planner.setRoot(SampleCases.CASE_1)
      val result = planner.findBestExp()
      println(RelOptUtil.dumpPlan("Result", result, SqlExplainFormat.TEXT, SqlExplainLevel.EXPPLAN_ATTRIBUTES))
    }
    "replace green by no color" in {
      val planner = hepPlanner(Set(RuleForGreen.REPLACED_BY_RULE))
      planner.setRoot(SampleCases.CASE_1)
      val result = planner.findBestExp()
      println(RelOptUtil.dumpPlan("Result", result, SqlExplainFormat.TEXT, SqlExplainLevel.EXPPLAN_ATTRIBUTES))
    }
  }
}
