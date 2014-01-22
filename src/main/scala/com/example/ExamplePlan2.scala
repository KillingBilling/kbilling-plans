package com.example

import kbilling.model._

object ExamplePlan2 extends BillingPlan {
  val accounts = Map(
    "Bones" -> ServiceAccount(Map("SUM" -> Aggregate.SUM)),
    "CashbackPercent" -> ServiceAccount(Map("SUM" -> Aggregate.SUM)),

    "USD" -> PaymentAccount({a =>
      val normRate = BigDecimal(0.1)
      val cashback: BigDecimal =
        if (a("CashbackPercent.SUM") > 0) BigDecimal(1) - BigDecimal(0.01) * a("CashbackPercent.SUM")
        else 1

      a("Bones.SUM") * normRate * cashback
    })
  )
  val notifications: Map[String, Notification] = Map.empty
}
