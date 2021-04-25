package com.knolx.rule

import com.google.common.collect.ImmutableList
import com.knolx.`trait`.ColorTrait
import com.knolx.physical.{CustomRel, RuledRel}
import com.knolx.rule
import org.apache.calcite.plan.RelRule.{MatchHandler, OperandTransform}
import org.apache.calcite.plan.{RelOptRule, RelOptRuleCall, RelRule}
import org.apache.calcite.rel.RelNode
import org.apache.calcite.rel.core.RelFactories
import org.apache.calcite.tools.RelBuilderFactory

class RuleForGreen(config: RuleForGreen.Config) extends RelRule[RuleForGreen.Config](config) {
  override def onMatch(call: RelOptRuleCall): Unit = config.matchHandler().accept(this, call)
}

object RuleForGreen {

  val PRECEDED_BY_RULE: RelOptRule = withOperandFor(addParent()).toRule
  val SUCCEEDED_BY_RULE: RelOptRule = withOperandFor(addChild()).toRule
  val REPLACED_BY_RULE: RelOptRule = withOperandFor(replace()).toRule

  def withOperandFor(handler: MatchHandler[RuleForGreen]): RelRule.Config =
    new RuleForGreen.Config()
      .withMatchHandler(handler)
      .withRelBuilderFactory(RelFactories.LOGICAL_BUILDER)
      .withOperandSupplier(b0 =>
        b0.operand(classOf[CustomRel]).`trait`(ColorTrait.Green).anyInputs())


  def addParent(): RelRule.MatchHandler[RuleForGreen] =
    (_, call) => {
      val rel: RelNode = call.rel(0)
      val originalTraits = rel.getTraitSet
      val enforcedTraits = originalTraits.replace(ColorTrait.None)
      val enforce = new RuledRel(rel.getCluster, originalTraits, rel.copy(enforcedTraits, rel.getInputs))
      call.transformTo(enforce)
    }

  def addChild(): RelRule.MatchHandler[RuleForGreen] = (_, call) => {
    val rel: RelNode = call.rel(0)
    val originalTraits = rel.getTraitSet
    val enforcedTraits = originalTraits.replace(ColorTrait.None)
    val enforce = new RuledRel(rel.getCluster, originalTraits, rel.getInput(0))
    call.transformTo(rel.copy(enforcedTraits, ImmutableList.of(enforce)))
  }

  def replace(): RelRule.MatchHandler[RuleForGreen] = (_, call) => {
    val rel: RelNode = call.rel(0)
    val desiredTraits = rel.getTraitSet.replace(ColorTrait.None)
    call.transformTo(rel.copy(desiredTraits, rel.getInputs))
  }

  class Config extends RelRule.Config {

    private var factory: RelBuilderFactory = _
    private var text: String = "Fires on green color"
    private var supplier: OperandTransform = _
    private var _matchHandler: MatchHandler[rule.RuleForGreen] = _

    override def toRule: RelOptRule = new RuleForGreen(this)

    def matchHandler[RuleForGreen](): MatchHandler[rule.RuleForGreen] = this._matchHandler

    def withMatchHandler[RuleForGreen](matchHandler: RelRule.MatchHandler[rule.RuleForGreen]): RuleForGreen.Config = {
      this._matchHandler = matchHandler
      this
    }

    override def relBuilderFactory(): RelBuilderFactory = this.factory

    override def withRelBuilderFactory(factory: RelBuilderFactory): RelRule.Config = {
      this.factory = factory
      this
    }

    override def description(): String = this.text

    override def withDescription(description: String): RelRule.Config = {
      this.text = description
      this
    }

    override def operandSupplier(): RelRule.OperandTransform = this.supplier

    override def withOperandSupplier(transform: RelRule.OperandTransform): RelRule.Config = {
      this.supplier = transform
      this
    }
  }

}