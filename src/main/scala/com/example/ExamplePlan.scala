package com.example

import kbilling.model._

object ExamplePlan extends BillingPlan {

  val accounts = Map(
    "Bones" -> ServiceAccount(Map(
      "SUM" -> Aggregate({_ + _}, {_ => 0})
    )),

    "USD" -> PaymentAccount({a =>
      val normRate = BigDecimal(0.1)
      val discountRate = BigDecimal(0.05)
      val t = BigDecimal(4)

      a("Bones.SUM") match {
        case x if x <= t => x * normRate
        case x if x > t => t * normRate + (x - t) * discountRate
      }
    })
  )

  val notifications = Set[Notification](
    Notification(a => a("USD") == 0, "zeroBalance")
  )

}
