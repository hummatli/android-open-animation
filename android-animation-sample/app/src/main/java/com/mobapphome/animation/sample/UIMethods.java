package com.mobapphome.animation.sample;

import android.view.View;

public class UIMethods {
	static public int getRelativeLeft(View myView) {
		if (myView.getParent() == myView.getRootView())
	        return myView.getLeft();
	    else
	        return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}

	static public int getRelativeTop(View myView) {
	    if (myView.getParent() == myView.getRootView())
	        return myView.getTop();
	    else
	        return myView.getTop() + getRelativeTop((View) myView.getParent());
	}
	
	static private float getCordSysDiffY(View myView) {
		return getRelativeTop(myView) - myView.getY();
	}

	static private float getCordSysDiffX(View myView) {
		return getRelativeLeft(myView) - myView.getX();
	}

	static public float mapToLocCordY(float yGlobal, View myView) {
		return yGlobal - getCordSysDiffY(myView);
	}

	static public float mapToLocCordX(float xGlobal, View myView) {
		return xGlobal - getCordSysDiffX(myView);
	}

}
