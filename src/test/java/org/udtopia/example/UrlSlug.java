package org.udtopia.example;

import org.udtopia.Value;
import org.udtopia.pure.PureString;
import org.udtopia.rules.LowerCase;
import org.udtopia.rules.Matching;
import org.udtopia.rules.Max;
import org.udtopia.rules.Trim;

@Trim @LowerCase @Max(40) @Matching("[a-z0-9-]+")
public final @Value class UrlSlug extends PureString<UrlSlug>
{
	public UrlSlug(final String slug) { super(UrlSlug::new, slug); }
}
