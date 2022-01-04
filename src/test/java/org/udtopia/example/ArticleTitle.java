package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureString;

public final @Value class ArticleTitle extends PureString<ArticleTitle>
{
	public ArticleTitle(final String title) { super(title); }
}
