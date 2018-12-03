package arrow.validation.refinedTypes.numeric

import arrow.Kind
import arrow.core.Either
import arrow.core.EitherPartialOf
import arrow.data.Nel
import arrow.data.Validated
import arrow.data.ValidatedPartialOf
import arrow.extension
import arrow.instances.either.applicativeError.applicativeError
import arrow.instances.nonemptylist.semigroup.semigroup
import arrow.instances.validated.applicativeError.applicativeError
import arrow.typeclasses.ApplicativeError
import arrow.validation.RefinedPredicateException
import arrow.validation.Refinement

internal fun <A : Number> isNonZero(a: A): Boolean = a != 0

/**
 * `NonZero` defines a subset of Numbers which are not 0
 */
interface NonZero<F, A : Number> : Refinement<F, A> {

  override fun A.refinement(): Boolean = isNonZero(this)

  /**
   * Commented method or class
   *
   * {: data-executable='true'}
   *
   * ```kotlin:ank
   * import arrow.validation.refinedTypes.numeric.*
   *
   * fun main(args: Array<String>) {
   *   //sampleStart
   *   val result = 0.nonZero()
   *  //sampleEnd
   *
   *  println(result.isValid)
   *  }
   *  ```
   */
  fun A.nonZero(): Kind<F, A> = refine(this)

  fun <B> A.nonZero(f: A.() -> B): Kind<F, B> = refine(this, f)

  companion object {
    fun errorMsg(x: Number): String = "$x cannot be 0"
  }

}

@extension
interface ValidatedNonZero<A : Number> :
  NonZero<ValidatedPartialOf<Nel<RefinedPredicateException>>, A> {
  override fun applicativeError(): ApplicativeError<ValidatedPartialOf<Nel<RefinedPredicateException>>,
    Nel<RefinedPredicateException>> =
    Validated.applicativeError(Nel.semigroup())

  override fun invalidValueMsg(a: A): String = NonZero.errorMsg(a)
}

@extension
interface EitherNonZero<A : Number> :
  NonZero<EitherPartialOf<Nel<RefinedPredicateException>>, A> {
  override fun applicativeError(): ApplicativeError<EitherPartialOf<Nel<RefinedPredicateException>>,
    Nel<RefinedPredicateException>> =
    Either.applicativeError()

  override fun invalidValueMsg(a: A): String = NonZero.errorMsg(a)
}