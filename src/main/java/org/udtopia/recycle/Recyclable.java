package org.udtopia.recycle;

/**
 * An effectively-immutable class with the ability to recycle its instances.
 * Implementing classes should maintain a mutable boolean state to track whether an instance is in use, or discarded to
 * the pool.
 */
public interface Recyclable
{
	/** @return {@code true} if this instance has been discarded by calling {@link #discard()}. */
	boolean isDiscarded();

	/** Flag this instance as discarded. It must not be used again, until recycled by the pool. */
	void discard();
}
