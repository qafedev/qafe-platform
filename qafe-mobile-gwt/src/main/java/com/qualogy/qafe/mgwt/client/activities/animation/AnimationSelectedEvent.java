/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.mgwt.client.activities.animation;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;

public class AnimationSelectedEvent extends Event<AnimationSelectedEvent.Handler> {

	public interface Handler {
		void onAnimationSelected(AnimationSelectedEvent event);
	}

	private static final Type<AnimationSelectedEvent.Handler> TYPE = new Type<AnimationSelectedEvent.Handler>();
	private final Animation animation;

	public static void fire(EventBus eventBus, Animation animation) {
		eventBus.fireEvent(new AnimationSelectedEvent(animation));
	}

	@Override
	public com.google.web.bindery.event.shared.Event.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	protected AnimationSelectedEvent(Animation animation) {
		this.animation = animation;

	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onAnimationSelected(this);

	}

	public static Type<AnimationSelectedEvent.Handler> getType() {
		return TYPE;
	}

	public Animation getAnimation() {
		return animation;
	}

}
