/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.dobots.picturetransformmodule;

public class vector_int {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected vector_int(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(vector_int obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AIMJNI.delete_vector_int(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public vector_int() {
    this(AIMJNI.new_vector_int__SWIG_0(), true);
  }

  public vector_int(long n) {
    this(AIMJNI.new_vector_int__SWIG_1(n), true);
  }

  public long size() {
    return AIMJNI.vector_int_size(swigCPtr, this);
  }

  public long capacity() {
    return AIMJNI.vector_int_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    AIMJNI.vector_int_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return AIMJNI.vector_int_isEmpty(swigCPtr, this);
  }

  public void clear() {
    AIMJNI.vector_int_clear(swigCPtr, this);
  }

  public void add(int x) {
    AIMJNI.vector_int_add(swigCPtr, this, x);
  }

  public int get(int i) {
    return AIMJNI.vector_int_get(swigCPtr, this, i);
  }

  public void set(int i, int val) {
    AIMJNI.vector_int_set(swigCPtr, this, i, val);
  }

}
