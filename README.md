<h1 align="center">
<picture>
  <source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/1f3c6/512.webp" type="image/webp">
  <img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f3c6/512.gif" alt="🏆" width="32" height="32">
</picture> Competition winning AI player <picture>
  <source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/1f3c6/512.webp" type="image/webp">
  <img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f3c6/512.gif" alt="🏆" width="32" height="32">
</picture>
</h1>

## ℹ️ Context ℹ️

I took the INF2050 (Software development tools and practices) course during the winter 2025 semester at UQAM University. The class was working on a semester long project in teams. At the end of the semester, the professor organized a competition between teams where the teams that wanted to participate would submit a Java file implementation of an interface that is an AI player for the Halma board game.

My teammates and I agreed that I would submit the implementation in my name as they decided not to participate in that particular competition. The AIs of the teams that participated played the game against each other. My AI ranked 3rd place.

## 💡 About my algorithm 💡

My implementation file is in [`src/main/java/ca/uqam/info/solanum/students/halma/aibattle/RobotMoveSelectorImpl.java`](src/main/java/ca/uqam/info/solanum/students/halma/aibattle/RobotMoveSelectorImpl.java). I map every move to the shortest distance between the target of the move and the closest target field. Then, I get the shortest of the mapped distances. Finally, I return any move that has that distance.

### ⏱️ Time complexity ⏱️

The time complexity is

$$\mathcal{O}\left(N^2\right) \text{ where}$$

$$N=\max{\left(M, T\right)} \text{ where}$$

$$M=\text{number of all possible moves and}$$

$$T=\text{number of target fields.}$$