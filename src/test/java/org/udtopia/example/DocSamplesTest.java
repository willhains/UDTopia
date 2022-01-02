package org.udtopia.example;

import org.apache.commons.text.WordUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Code samples from documentation, and other examples of example usage.
 */
public class DocSamplesTest
{
	@Test public void shouldConvertTitleToSlug()
	{
		final ArticleTitle title = new ArticleTitle(" test article title. ");
		final ArticleTitle trimmed = title.map(String::trim);
		final ArticleTitle capitalized = trimmed.map(WordUtils::capitalizeFully);
		final ArticleTitle noDot = capitalized.map(t -> t.replaceAll("\\.$", ""));
		assertThat(noDot.get(), is("Test Article Title"));

		final UrlSlug urlSlug = noDot.map(t -> t.replaceAll("\\s", "-"), UrlSlug::new);
		assertThat(urlSlug.get(), is("Test-Article-Title"));
	}

	@Test public void shouldMultiplyTwoDifferentUDTs()
	{
		// This is only an illustration
		// Don't use floating-point values for money!
		final Quantity orderQuantity = new Quantity(5);
		final Price unitPrice = new Price(24.95);
		final Price orderTotal = unitPrice.multiplyBy(orderQuantity);
		final Price withTax = orderTotal.multiplyBy(1.15);
		assertThat(withTax.getAsDouble(), is(closeTo(143.4625, 0.0001)));
	}
}
