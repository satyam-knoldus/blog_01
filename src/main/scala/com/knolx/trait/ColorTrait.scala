package com.knolx.`trait`

import com.knolx.traitdef.ColorDef
import org.apache.calcite.plan.{RelOptPlanner, RelTrait, RelTraitDef}

sealed trait ColorTrait extends RelTrait {
  val color: String
}

abstract class Color(val color: String) extends ColorTrait {
  override def getTraitDef: RelTraitDef[_ <: RelTrait] = ColorDef

  override def register(planner: RelOptPlanner): Unit = {
    //we will register rule another way
  }

  override def satisfies(`trait`: RelTrait): Boolean = {
    return this == `trait` ||
      (isColorTrait(`trait`) && ColorTrait.satisfies(this.color, `trait`.asInstanceOf[ColorTrait].color))
  }

  override def toString: String = this.color

  def isColorTrait(relTrait: RelTrait): Boolean = relTrait.isInstanceOf[ColorTrait]
}

/**
 * We introduce custom hierarchy.
 * None
 *  - Red
 *    --  Green
 *    ---  Blue
 *
 * A color only satisfies its parents.
 */
object ColorTrait {

  val satisfyCondition = Map[String, Seq[String]](
    "BLUE" -> Seq("NONE", "RED", "GREEN", "BLUE"),
    "GREEN" -> Seq("NONE", "RED", "GREEN"),
    "RED" -> Seq("NONE", "RED"),
    "NONE" -> Seq()
  )

  case object Red extends Color("RED")

  case object Green extends Color("GREEN")

  case object Blue extends Color("BLUE")

  case object None extends Color("NONE")

  def satisfies(colorA: String, colorB: String): Boolean = {
    satisfyCondition.get(colorA) match {
      case Some(list) => list.contains(colorB)
      case _ => false
    }
  }
}


