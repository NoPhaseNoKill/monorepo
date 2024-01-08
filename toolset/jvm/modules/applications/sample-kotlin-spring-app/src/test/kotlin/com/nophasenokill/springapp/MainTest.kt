package com.nophasenokill.springapp

import com.nophasenokill.lib.OldMessageModel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MainTest(
  private val main: Main,
) : ShouldSpec({

  context("the main") {
    should("run") {
      assertDoesNotThrow { main.run() }
      true shouldBe true
    }
  }

  context("message model") {
    should("have null value for its message in no args constructor") {
      val message = OldMessageModel().message
      message shouldBe null
    }

    should("have have value for its message when passed to constructor") {
      val message = OldMessageModel("actual value").message
      message shouldBe "actual value"
    }
  }
})
