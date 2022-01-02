package org.udtopia.recycle;

import org.udtopia.Value;

/**
 * A function that privately mutates a discarded instance.
 *
 * @param <Discarded> the class of objects this recycler can privately mutate.
 */
@FunctionalInterface
public @Value interface Recycler<Discarded extends Recyclable>
{
	/**
	 * Privately mutate the specified discarded object.
	 *
	 * @param discarded an instance that has been {@linkplain Recyclable#discard discarded} and will never be
	 * 	referenced again.
	 */
	void recycle(Discarded discarded);
}
