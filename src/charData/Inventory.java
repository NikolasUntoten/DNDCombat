package charData;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import charData.items.Item;

public class Inventory implements List<Item>, Serializable {
	
	private Item[] items;
	private int size;
	private double maxWeight;
	
	public Inventory(double initMaxWeight) {
		items = new Item[10];
		maxWeight = initMaxWeight;
		size = 0;
	}
	
	public boolean hasSpaceFor(double w) {
		return getWeight() + w < maxWeight;
	}
	
	public double getWeight() {
		double sum = 0.0;
		for (int i = 0; i < size; i++) {
			sum += items[i].getWeight();
		}
		return sum;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size <= 0;
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			if (o.equals(items[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Item[size];
		for (int i = 0; i < size; i++) {
			arr[i] = items[i];
		}
		return arr;
	}

	@Override
	public boolean add(Item e) {
		if (getWeight() + e.getWeight() > maxWeight) return false;
		
		if (hasRoom()) {
			items[size] = e;
			size++;
			return true;
		} else {
			makeRoom();
			return add(e);
		}
	}
	
	private boolean hasRoom() {
		return size < items.length;
	}
	
	private void makeRoom() {
		Item[] arr = new Item[size + 10];
		for (int i = 0; i < size; i++) {
			arr[i] = items[i];
		}
		items = arr;
	}

	@Override
	public boolean remove(Object o) {
		if (this.contains(o)) {
			int index = this.indexOf(o);
			for (int i = index; i < size - 1; i++) {
				items[i] = items[i + 1];
			}
			size--;
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		items = new Item[10];
		size = 0;
	}

	@Override
	public Item get(int index) {
		if (index < size && index >= 0) {
			return items[index];
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Item set(int index, Item element) {
		Item removed = items[index];
		items[index] = element;
		return removed;
	}

	@Override
	public void add(int index, Item element) {
		if (getWeight() + element.getWeight() > maxWeight) return;
		if (!hasRoom()) makeRoom();
		size++;
		for (int i = size - 1; i > index; i++) {
			items[i] = items[i - 1];
		}
		items[index] = element;
		
	}

	@Override
	public Item remove(int index) {
		if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
		Item removed = items[index];
		for (int i = index; i < size - 1; i++) {
			items[i] = items[i + 1];
		}
		size--;
		return removed;
	}
	
	public Item[] toItemArray() {
		Item[] arr = new Item[size];
		for (int i = 0; i < size; i++) {
			arr[i] = items[i];
		}
		return null;
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Item> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Item> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Item> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Item> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<Item> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
