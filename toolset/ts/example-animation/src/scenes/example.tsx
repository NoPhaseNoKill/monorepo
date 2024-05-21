import {Circle, Rect, makeScene2D} from '@motion-canvas/2d';
import {createRef} from '@motion-canvas/core';

export default makeScene2D(function* (view) {
  // Create your animations here

  view.add(<Rect size={1920}
                 radius={40}
                 fill={'grey'} />);

  const circle = createRef<Circle>();

  view.add(<Circle ref={circle} size={160} fill={'lightseagreen'} />);
  yield* circle().x(400).y(-350).scale(2,2).to(1, 2)

  const circle2 = createRef<Circle>();
  view.add(<Circle ref={circle2} size={160} fill={'lightseagreen'} />);
  yield* circle2().x(-400).y(350).scale(2,2).to(1, 2)
});


