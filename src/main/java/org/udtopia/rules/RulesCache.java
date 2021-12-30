package org.udtopia.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BinaryOperator;
import org.udtopia.Mutable;
import org.udtopia.ThreadSafe;

import static java.text.MessageFormat.*;
import static java.util.Arrays.*;
import static org.udtopia.rules.ApplyRuleWhen.*;

/**
 * Lazily-initialized cache of rules for each annotated class.
 *
 * @param <RuleType> {@link IntRule}, {@link LongRule}, {@link DoubleRule}, {@link StringRule}, etc.
 */
final @ThreadSafe @Mutable class RulesCache<RuleType> extends ClassValue<RuleType>
{
	private final Class<RuleType> _ruleType;
	private final RuleType _nullRule;
	private final BinaryOperator<RuleType> _ruleCombiner;

	RulesCache(final Class<RuleType> ruleType, final RuleType nullRule, final BinaryOperator<RuleType> ruleCombiner)
	{
		_ruleType = ruleType;
		_nullRule = nullRule;
		_ruleCombiner = ruleCombiner;
	}

	/** Build a chain of rules from the annotations declared on the given class. */
	@SuppressWarnings("unchecked")
	@Override protected RuleType computeValue(final Class<?> annotatedClass)
	{
		// Get the annotations declared on the class
		return stream(annotatedClass.getAnnotations())

			// Conditional application of rules, such as `when=ASSERTS_ENABLED`
			.filter(annotation -> shouldApplyRule(annotation, annotatedClass))

			// Find nested classes in the annotation that implement the rule interface
			.flatMap(annotation -> stream(annotation.annotationType().getDeclaredClasses())
				.filter(_ruleType::isAssignableFrom)
				.map(ruleClass -> (Class<? extends RuleType>) ruleClass)

				// Create a rule instance from the annotation details
				.map(ruleClass -> _buildRule(annotation, ruleClass)))

			// Link all the rules together in a chain
			.reduce(_nullRule, _ruleCombiner);
	}

	private RuleType _buildRule(final Annotation annotation, final Class<? extends RuleType> ruleClass)
	{
		final Class<? extends Annotation> annotationType = annotation.annotationType();
		try { return ruleClass.getConstructor(annotationType).newInstance(annotation); }
		catch (final NoSuchMethodException ignored)
		{
			throw new RulesError(format(
				"Cannot attach @{0} rule. Annotation {0} must have a nested {1} implementation class, " +
					"with a public constructor having a single argument of type {0}.",
				annotationType.getSimpleName(),
				_ruleType.getSimpleName()));
		}
		catch (final InvocationTargetException | InstantiationException | IllegalAccessException e)
		{
			throw new RulesError(e, format(
				"Cannot attach @{0} rule. Exception in {1} constructor.",
				annotationType.getSimpleName(),
				ruleClass.getSimpleName()));
		}
	}

	@Override public void remove(final Class<?> type)
	{
		throw new UnsupportedOperationException("Cannot remove rules");
	}

	@Override public String toString()
	{
		return format("{0}<{1}>", getClass().getSimpleName(), _ruleType.getSimpleName());
	}
}
