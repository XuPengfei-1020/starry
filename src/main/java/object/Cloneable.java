package object;

/**
 * Custom cloneable interface.
 *
 * @author flying
 */
public interface Cloneable<T> extends java.lang.Cloneable {
    /**
     * Clone a instance same as this.
     * @return a copied instance same as this but not this.
     */
    T clone();
}