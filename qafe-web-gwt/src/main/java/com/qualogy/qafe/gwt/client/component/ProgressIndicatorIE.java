/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.util.QEffects;

public class ProgressIndicatorIE extends ProgressIndicator {

	public class CircleAnimation extends Animation {
		
		private AbsolutePanel container = null;
		private List<Widget> widgets = null;

		private int centerX = 8;
		private int centerY = 8;
		private int radius = 8;
		
		private double interval = 0;
		private int duration = -1;
		private boolean loop = false;
		
		public CircleAnimation(AbsolutePanel container) {
			this.container = container;
			widgets = new ArrayList<Widget>();			
			widgets.add(getImageBundle().dotDark().createImage());
			widgets.add(getImageBundle().dotMedium().createImage());
			widgets.add(getImageBundle().dotLight().createImage());
			widgets.add(getImageBundle().dotDark().createImage());
			widgets.add(getImageBundle().dotMedium().createImage());
			widgets.add(getImageBundle().dotLight().createImage());
			
			init();
		}

		private void init() {
			int numWidgets = widgets.size();
			if (numWidgets > 0) {
				interval = 2.0 / numWidgets;	
			}
			
			for (Widget widget : widgets) {
				container.add(widget);
			}
		}
		
		@Override
		public void run(int duration) {
			start(duration, false);
		}
		
		public void start(int duration, boolean loop) {
			this.duration = duration;
			this.loop = loop;
			super.run(duration);
		}

		public void stop() {
			loop = false;
			super.cancel();
		}

		@Override
		protected void onStart() {
			super.onStart();
		}

		@Override
		protected void onUpdate(double progress) {
			double radian = 2 * Math.PI * progress;
			for (int i=0; i<widgets.size(); i++) {
				Widget widget = widgets.get(i);
				double offset = i * interval;
				updatePosition(widget, radian, offset * Math.PI);
			}
		}
		
		@Override
		protected void onComplete() {
			super.onComplete();
			if (loop) {
				start(duration, loop);
			}
		}
		
		/**
		 * Update the position of the widget, adding a rotational offset.
		 */
		private void updatePosition(Widget widget, double radian, double offset) {
			radian += offset;
			double x = radius * Math.cos(radian) + centerX;
			double y = radius * Math.sin(radian) + centerY;
			container.setWidgetPosition(widget, (int)x, (int)y);
		}
	}

	private CircleAnimation circleAnimation;
	
	public ProgressIndicatorIE() {
		clear();
		circleAnimation = new CircleAnimation(this);				
	}

	@Override
	public void start() {
		QEffects.fadeIn(this, 500, 50);
		circleAnimation.start(3000, true);
	}

	@Override
	public void stop() {
		QEffects.fadeOut(this, 500, 50, false);
		circleAnimation.stop();
	}
}