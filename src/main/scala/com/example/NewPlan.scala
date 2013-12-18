package com.example

import kbilling.types._

object NewPlan {

  val bones = new ServiceAccount {

    val sum = aggregates.sum

  }

  val usd = new PaymentAccount {

    def apply(bonesSum: BigDecimal): BigDecimal = {
      val normRate = BigDecimal(0.1)
      val discountRate = BigDecimal(0.05)
      val t = BigDecimal(4)

      bonesSum match {
        case x if x <= t => x * normRate
        case x if x > t => t * normRate + (x - t) * discountRate
      }
    }

  }

  def zeroBalance(usd: BigDecimal): Boolean = notifications.zero(usd)

}
