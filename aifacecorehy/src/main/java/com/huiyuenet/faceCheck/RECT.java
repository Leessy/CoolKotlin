package com.huiyuenet.faceCheck;

public class RECT {
	public int left;
	public int top;
	public int right;
	public int bottom;

	public void copyFrom(RECT rect){
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
    }
}
