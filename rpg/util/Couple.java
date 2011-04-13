package rpg.util;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class representing a 'couple' or unordered 2-tuple. Null is allowed, 
 * but  only once (so it's basically a bag with a minimum capacity of 1 and 
 * a maximum capacity of 2).
 *
 * @invar
 * At least one of the elements of each couple is not null.
 *   | getNbElements() &gt;= 1
 *
 * @author Roald Frederickx
 */
public class Couple<Type> implements Iterable<Type> {
	/** 
	 * Create a new couple with the given element.
	 * 
	 * @param element 
	 * The element to add to the new couple.
	 * @effect
	 *   | this(element.null)
	 */
	@Raw
	public Couple(Type element) {
		this(element, null);
	}

	/** 
	 * Create a new couple with the given elements.
	 * 
	 * @param element1
	 * The first element.
	 * @param element2 
	 * The second element.
	 * @pre
	 * At least one of the given elements is not null.
	 * @post
	 * This new couple contains the given elements.
	 */
	@Raw
	public Couple(Type element1, Type element2) {
		assert (element1 != null) || (element2 != null);
		elements[0] = element1;
		elements[1] = element2;
	}

	/** 
	 * Returns an element of this couple.
	 * Note that if (getNbElements() == 1), this is <i>the</i> element.
	 * If (getNbElements() == 2), the other element can be found via 
	 * getPartner().
	 * 
	 * @return 
	 * An element of the couple.
	 */
	public Type getAnElement() {
		if (elements[0] != null)
			return elements[0];
		return elements[1];
	}

	/**
	 * Adds the given element to this couple as a second member.
	 *
	 * @pre
	 *   | getNbElements() == 1
	 * @post
	 * The given element gets added as a second member of the new couple.
	 */
	public void add(Type element) {
		assert getNbElements() == 1;
		setPartner(getAnElement(), element);
	}

	/** 
	 * Returns whether or not the given element is a member of this couple.
	 *
	 * @param element
	 * The element to searcd for.
	 * @pre
	 *   | element != null
	 * @return
	 * True iff the given element is a member of this couple.
	 */
	public boolean contains(Type element) {
		assert (element != null);
		return element.equals(elements[0]) || (element.equals(elements[1]));
	}

	/** 
	 * Returns the 'partner' of the given element in this couple.
	 * 
	 * @param thisElement 
	 * The element whose partner will be returned.
	 * @pre
	 * The given element is not null.
	 * @return
	 * The partner of the given element.
	 * This will be null if the given element is not a member of this 
	 * couple, or if the given element is the only element of this couple.
	 */
	public Type getPartner(Type thisElement) {
		assert thisElement != null;
		if (thisElement.equals(elements[0]))
			return elements[1];
		if (thisElement.equals(elements[1]))
			return elements[0];
		return null;
	}

	/** 
	 * Sets the 'partner' of the given element in this couple.
	 * 
	 * @param thisElement 
	 * The element whose partner will be returned.
	 * @param newPartner
	 * The new 'partner' of thisElement.
	 * @post
	 *   | getPartner(thisElement).equals(newOtherElement)
	 * @throws IllegalArgumentException
	 *   | thisElement == null
	 * @throws IllegalArgumentException
	 * 'thisElement' is not a member of this couple.
	 */
	public void setPartner(Type thisElement, Type newPartner) {
		if (thisElement == null)
			throw new IllegalArgumentException();
		if (thisElement.equals(elements[0]))
			elements[1] = newPartner;
		else if (thisElement.equals(elements[1]))
			elements[0] = newPartner;
		else
			throw new IllegalArgumentException();
	}

	/** 
	 * Deletes the given element from this couple
	 * 
	 * @param element 
	 * @pre
	 *   | element != null
	 * @pre
	 *   | getNbElements() == 2
	 * @throws IllegalArgumentException 
	 *   | !contains(element)
	 * @post
	 *   | !contains(element)
	 * @post
	 *   | getNbElements() == 1
	 */
	public void delete(Type element) throws IllegalArgumentException {
		assert element != null;
		assert getNbElements() == 2;
		if (elements[0].equals(element))
			elements[0] = null;
		else if (elements[1].equals(element))
			elements[1] = null;
		throw new IllegalArgumentException();
	}


	public int getNbElements() {
		int num = 0;
		if (elements[0] != null)
			num++;
		if (elements[1] != null)
			num++;
		return num;
	}

	/** 
	 * Variable referencing an array of elements representing the couple.
	 */
	private Type elements[] = (Type[]) new Object[2];

	/** 
	 * Returns an iterator over the elements of the couple.
	 * 
	 * @return 
	 * An iterator over the elements of the couple.
	 */
	public java.util.Iterator<Type> iterator() {
		Type iteratorElements[];

		if (elements[0] != null  &&  elements[1] != null)
			iteratorElements = (Type[]) elements.clone();
		else if (elements[0] != null)
			iteratorElements = (Type[]) new Object[] {elements[0]};
		else
			iteratorElements = (Type[]) new Object[] {elements[1]};

		return java.util.Arrays.asList(iteratorElements).iterator();
	}
}
