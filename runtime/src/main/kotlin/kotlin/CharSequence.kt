package kotlin

/**
 * Represents a readable sequence of [Char] values.
 */
public interface CharSequence {
    /**
     * Returns the length of this character sequence.
     */
    public val length: Int

    /**
     * Returns the character at the specified [index] in this character sequence.
     */
    public operator fun get(index: Int): Char

    /**
     * Returns a new character sequence that is a subsequence of this character sequence,
     * starting at the specified [startIndex] and ending right before the specified [endIndex].
     *
     * @param startIndex the start index (inclusive).
     * @param endIndex the end index (exclusive).
     */
    public fun subSequence(startIndex: Int, endIndex: Int): CharSequence
}