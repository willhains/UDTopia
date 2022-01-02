package org.udtopia.recycle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Customize the size of the {@linkplain RecycleBin instance pool}.
 * Without this annotation, the default size is {@value #DEFAULT_SIZE}.
 *
 * @see RingBufferSize
 * @see RingBufferRecycleBin
 */
@Target(TYPE) @Retention(RUNTIME)
public @interface RecycleBinSize
{
	/** Default size of the {@linkplain RecycleBin instance pool}. */
	int DEFAULT_SIZE = 16;

	/** @return size of the {@linkplain RecycleBin instance pool}. */
	int value();
}
