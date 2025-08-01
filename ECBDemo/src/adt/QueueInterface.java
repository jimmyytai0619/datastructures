/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package adt;

/**
 *
 * @author jimmy
 */
public interface QueueInterface<T> {
    void enqueue(T item);
    T dequeue();
    boolean isEmpty();
    T peek();
    int size();
    void clear();
}
