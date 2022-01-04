package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureString;

public final @Value class UrlSlug extends PureString<UrlSlug>
{
	public UrlSlug(final String slug) { super(UrlSlug::new, slug); }
}
