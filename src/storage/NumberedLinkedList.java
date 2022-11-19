/*  Accountable: a personal spending monitoring program
    Copyright (C) 2022  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package storage;

import java.util.*;

public class NumberedLinkedList<E> implements List<E>, Comparable<NumberedLinkedList<E>> {
    private final LinkedList<E> l;
    private int n;

    public NumberedLinkedList(LinkedList<E> l, int n) {
        this.l = l;
        this.n = n;
    }

    public int getNumber() {
        return n;
    }

    public void setNumber(int n) {
        this.n = n;
    }

    @Override
    public int compareTo(NumberedLinkedList<E> that) {
        return Integer.compare(this.n, that.n);
    }

    @Override
    public int size() {
        return l.size();
    }

    @Override
    public boolean isEmpty() {
        return l.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return l.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return l.iterator();
    }

    @Override
    public Object[] toArray() {
        return l.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return l.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return l.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return l.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return l.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return l.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return l.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return l.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return l.retainAll(c);
    }

    @Override
    public void clear() {
        l.clear();
    }

    @Override
    public E get(int index) {
        return l.get(index);
    }

    @Override
    public E set(int index, E element) {
        return l.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        l.add(index, element);
    }

    @Override
    public E remove(int index) {
        return l.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return l.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return l.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return l.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return l.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return l.subList(fromIndex, toIndex);
    }
}
