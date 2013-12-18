package com.example

import kbilling.types._

object NewPlan {

  val bones = new ServiceAccount {

    def bonesSum(a: BigDecimal, b: BigDecimal): BigDecimal = ???

  }

  val usd = new PaymentAccount {

    def cost(bonesSum: BigDecimal): BigDecimal = ???

  }

  def zeroBalance(usd: BigDecimal): Boolean = ???

  /*
  object accounts {

    "USD" -> PaymentAccount({a =>
      val normRate = BigDecimal(0.1)
      val discountRate = BigDecimal(0.05)
      val t = BigDecimal(4)

      a("BonesSUM") match {
        case x if x <= t => x * normRate
        case x if x > t => t * normRate + (x - t) * discountRate
      }
    })

  }
  */

  //val notifications = Set[Notification]()

}
