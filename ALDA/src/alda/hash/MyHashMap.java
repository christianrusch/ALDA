package alda.hash;

import java.util.Set;
import java.util.HashSet;

public class MyHashMap<K,V> {

	private class MyHashEntry<K,V> {

		K key;
		V value;
		MyHashEntry<K,V> next;

		public MyHashEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

	}

	private MyHashEntry<K,V>[] table;
	private Set<MyHashEntry<K,V>> entrySet = new HashSet<>();
	private Set<K> keySet = new HashSet<>();
//	private Set<V> values = new HashSet<>();
	private static final int DEFAULT_BUCKETS = 16;
	private int numberOfBuckets;
	private int size = 0;

	//	private final static long PRIME = 1949054221; // Prime no: 95841901

	public MyHashMap() {
		this(DEFAULT_BUCKETS);
	}
	
	public MyHashMap(int i) {
		table = new MyHashEntry[i];
		numberOfBuckets = i;
	}

	/**
	 * Re-hashes the incoming hash code to guard against poor hash codes.
	 * @param i The hash code obtained from the key class.
	 * @return Returns a new hash code, obtained by performing a bitwise
	 * AND operation on i and the largest possible Integer value
	 * (2^31 - 1) to ensure handling of negative values for i.
	 */
	private int rehash(int i) {
		return (i & Integer.MAX_VALUE) % table.length;
	}


	public int size() {
		return size;
	}


	public boolean isEmpty() {
		return size == 0;
	}


	public boolean containsKey(Object key) {
		return keySet.contains(key);
	}


//	public boolean containsValue(V value) {
//		return valueSet.contains(value);
//	}


	public V get(K key) {
		int index = rehash(key.hashCode());
		MyHashEntry<K,V> e = table[index];
		if (e != null) {
			for(; e.next != null; e = e.next) {
				if(e.key == key) {
					return e.value;
				}
			}
		}
		throw new java.util.NoSuchElementException("Key not found.");
	}


	public void put(K key, V value) {
		
		if(key.equals(null)) {		// cannot put null key into map
			return;
		}
		
		int index = rehash(key.hashCode());
		
		MyHashEntry<K,V> newEntry = new MyHashEntry<>(key, value);
		MyHashEntry<K,V> currentEntry = table[index];
		
		/**
		 * If the table index is not empty, step through the linked list
		 * and check if the key is present. If it is, remove it, re-link
		 * the entries in the list and exit the loop.
		 */
		if(currentEntry != null) {
			for(;currentEntry.next != null; currentEntry = currentEntry.next) {
				if(currentEntry.next.key.equals(key)) {
					entrySet.remove(currentEntry.next);
					keySet.remove(currentEntry.next.key);
					currentEntry.next = currentEntry.next.next;
					--size;
					break;
				}
			}
		}
		
		/**
		 * Set the new entry's next pointer to the first entry in the list
		 * or null, then insert the new entry at the beginning of the list.
		 */
		newEntry.next = table[index];
		table[index] = newEntry;
		keySet.add(key);
		entrySet.add(newEntry);
		++size;
		
		if(size > numberOfBuckets) {
			growMyHashTable();
		}
		
	}
	
	private void growMyHashTable() {
		MyHashEntry<K,V>[] oldTable = table;
		if(numberOfBuckets > (Integer.MAX_VALUE / 2)){
			return;
		}
		numberOfBuckets *= 2;
		MyHashEntry<K,V>[] newTable = new MyHashEntry[numberOfBuckets*2];
		for(int i = 0; i < oldTable.length; i++) {
			newTable[i] = oldTable[i];
		}
		table = newTable;
	}


	public K remove(K key) {
		int index = rehash(key.hashCode());
		MyHashEntry<K,V> e = table[index];
		MyHashEntry<K,V> removed = null;
		if (e != null) {
			for(; e.next != null; e = e.next) {
				if(e.next.key.equals(key)) {
					removed = e.next;
					entrySet.remove(removed);
					keySet.remove(removed.key);
					e.next = e.next.next;
					--size;
					return removed.key;
				}
			}
		}
		throw new java.util.NoSuchElementException("Key not found.");
	}

	public void clear() {
		for(int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		size = 0;
	}
	
	public Set<K> keySet() {
		return keySet;
	}
	
//	public Set<V> values() {
//		return values;
//	}
	
}
