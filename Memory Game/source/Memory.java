import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Memory extends PApplet {




int numCards = 2;
ArrayList<Card> cards = new ArrayList<Card>();
int firstGuessId = -1;
int timer = 0;
int hideCardsTimer = 0;
int correctGuessTimer = 0;
PImage[] pics;
PImage cardBack;
PImage tableCloth;
boolean winFlag = false;
Button[] buttons = new Button[4];
PFont font; 
int score = 0;


//PImage zero;


public void setup() {
  //size(450, 800);
  
  //size(1280, 720);
  //fullScreen();
  frameRate(30);
  createBoard();
  cardBack = loadImage("cardBack.jpg");
  tableCloth = loadImage("tableCloth.jpg");
  font = createFont("Arial Bold", 120, true);
  textFont(font);
  //zero = loadImage("IMG_1305.JPG");
  
  if (buttons != null){
    for (int i = 0; i < 4; i++){
      buttons[i] = new Button(i * width / 5 + width / 10, height / 8,
      width / 6, height / 10, i);
    }
  }
  
  //String path =  "/data/";
  //File[] files = listFiles(path);
  //shuffleArray(files);
  //for (File i: files){
  //  println("file: ", i);
  //}
  //pics = new PImage[numCards];
  //for (int i = 0; i < numCards; i++){
  //  pics[i] = loadImage(files[i].getName());
  //  //println(files[i].getName());
  //}
  String[] pictures = {
    "001.jpg",
    "005.jpg",
    "036.JPG",
    "121.jpg",
    "131.jpg",
    "1516.jpg",
    "IMG_1318.JPG",
    "IMG_1314.JPG",
    "IMG_1316.JPG",
    "IMG_1315.JPG",
    "IMG_1312.JPG",
    "IMG_1310.JPG",
    "IMG_1307.JPG",
    "IMG_1305.JPG",
  };
  
  // if we shuffle the images we can have different pics for different games
  ArrayList<String> pictures2= new ArrayList<String>(Arrays.asList(pictures));
  Collections.shuffle(pictures2);
  pics = new PImage[numCards];
  for (int i = 0; i < numCards; i++){
    pics[i] = loadImage(pictures2.get(i));
  }
  
}

public void draw() {
  background(0, 50, 120, 255);
  imageMode(CORNER);
  tableCloth.resize(width, height);
  background(tableCloth);
  //image(zero, 50, 50);
  if (! winFlag) {
    timer += 1;
  }
  hideCardsTimer -= 1;
  if (hideCardsTimer == 0){
    hideCards();
  }
  correctGuessTimer -= 1;
  if (correctGuessTimer == 0){
    correctGuess();
  }
  
  for (Card i: cards){
    i.show();
  }
  
  if (winFlag) {
    for (Button button: buttons){
      button.visible = true;
    }
    winner();
  }
  
  for (Button button: buttons){
    button.show();
  }
  
}

public void createBoard() {
  float nextX, nextY, gap;
  float cardWidth = min(height, width)/ sqrt(2 * numCards);
  gap = cardWidth / 5;
  nextX = gap;
  nextY = gap;
  int maxXId = 0;
  float maxX = 0;
  
  ArrayList<Integer> ids = new ArrayList<Integer>();
  
  // make a list of ids and shuffle them to use as param for new Cards
  for(int i = 0; i < numCards * 2; i++){
    if (i < numCards){
      ids.add(i);
    } else {
      ids.add(i - numCards);
    }
  }
  Collections.shuffle(ids);
  
  // Create Cards and put them in array: cards
  for (int i = 0; i < 2 * numCards; i++){
    if(nextX + gap + cardWidth > width){
      nextX = gap;
      nextY += (cardWidth * 3 / 4 + gap);
      maxXId = ids.get(i - 1);
      
    }
    cards.add(new Card(nextX, nextY, cardWidth, ids.get(i)));
    nextX += (gap + cardWidth);
  }
  
  // center the cards on the board
  // centering on the x is harder because the last card may not be farthest right
  while (true) {
    for (Card card: cards) {
      if (card.x > maxX) {
        maxX = card.x;
      }
    }
    if (cards.get(0).x < width - (maxX + cards.get(0).w + gap / 2)){
      for (Card card: cards) {
        card.x += gap / 8;
      }
    } else {
      break;
    }
  }
  
  // center the y
  while (cards.get(0).y < height - (cards.get(cards.size() - 1).y + cards.get(0).h + gap)){
    for (Card card: cards) {
      card.y += gap / 8;
    }
  }
}


public void mousePressed(){
  if (winFlag){
    for (Button button: buttons) {
      if (button.checkPressed()){
        button.action();
      }
    }
  }
  //println(timer, "mouse clicked ", mouseX, ":", mouseY);
  //if (winFlag && mouseX < width / 2 && mouseY < height / 2){
  //  newGame(8);
  //  return;
  //} else if (winFlag && mouseX > width / 2 && mouseY < height / 2){
  //  newGame(10);
  //  return;
  //} else if (winFlag && mouseX < width / 2 && mouseY > height / 2){
  //  newGame(12);
  //  return;
  //} else if (winFlag){
  //  newGame(14);
  //  return;
  //}
  if (hideCardsTimer > 0){
    hideCardsTimer = 1;
    return; 
  } else if (correctGuessTimer > 0) {
    correctGuessTimer = 1;
    return;
  } else if (timer < 5) {
    return;
  }
  for (int i = cards.size() - 1; i >= 0; i--){
    if (cards.get(i).x < mouseX && cards.get(i).x + cards.get(i).w > mouseX
      && cards.get(i).y < mouseY && cards.get(i).y + cards.get(i).w > mouseY
      && ! cards.get(i).visible
    ){
      cards.get(i).visible = true;
      checkGuess(cards.get(i).id);
      return;
    }
  }
}

public void checkGuess(int id){
  if (firstGuessId == -1){
    firstGuessId = id;
  } else if (firstGuessId == id) {
    // do stuff for correct answer
    correctGuessTimer = 60;
    score += numCards * 5;
  } else{
    // do stuff for incorrect answer
    firstGuessId = -1;
    hideCardsTimer = 90;
    score -= numCards * 1;
  }
}

public void hideCards() {
  for (Card card: cards) {
    card.visible = false;
  }
}

public void correctGuess() {
  if (cards.size() == 2){
    winFlag = true;
    return;
  }
  for (int i = cards.size() - 1; i >= 0; i--) {
    if (cards.get(i).id == firstGuessId){
      cards.remove(i);
    }
  }
  firstGuessId = -1;
}

private static void shuffleArray(File[] array)
{
    int index;
    File temp;
    Random random = new Random();
    for (int i = array.length - 1; i > 0; i--)
    {
        index = random.nextInt(i + 1);
        temp = array[index];
        array[index] = array[i];
        array[i] = temp;
    }
}

public void winner(){
  pushStyle();
  textAlign(CENTER, BOTTOM);
  float bigness = min(width, height) / 5;
  textSize(bigness);
  fill(255, 0, 0, 255);
  text("You Win!", width / 2, height / 2);
  textAlign(CENTER, TOP);
  fill(255, 255, 255, 255);
  textSize(bigness / 2);
  text("score: "+ str(score), width / 2, height / 2);
  //text("seconds: "+ str(timer / 30), width / 2, height / 2);
  textAlign(CENTER, CENTER);
  text("Start New Game", width / 2, height / 16);

  
  stroke(200, 200, 200, 50);
  line (width / 2, 0, width / 2, height);
  line(0, height / 2, width, height / 2);
  popStyle();
  
  
  //noLoop();
}

public void newGame(int tempNumCards){
  numCards = tempNumCards;
  cards.clear();
  timer = 0;
  setup();
  firstGuessId = -1;
  correctGuessTimer = 0;
  hideCardsTimer = 0;
  winFlag = false;
  score = 0;
  //loop();
}
class Button {
  float x;
  float y;
  float w;
  float h;
  int id;
  boolean visible;
  
  Button(float x, float y, float w, float h, int id){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.id = id;
    this.visible = false;
  }
  
  public void show(){
    if (! this.visible){
      return;
    }
    pushStyle();
    stroke(184, 134, 11, 255);
    if (mouseX > this.x && mouseX < this.x+this.w
    && mouseY > this.y && mouseY < this.y + this.h
    ){
      fill(255, 215, 0, 255);
      stroke(218, 165, 0, 255);
    } else {
      fill(218, 165, 0, 255);
    }
    
    strokeWeight(this.h / 30);
    rect(this.x, this.y, this.w, this.h, this.w / 8);
    textAlign(CENTER, CENTER);
    textSize(this.h / 1.5f);
    fill(255);
    text(8 + 2 * this.id, this.x + this.w / 2, this.y + this.h / 2.5f);
    popStyle();
  }
  
  public boolean checkPressed() {
    if (mouseX > this.x && mouseX < this.x+this.w
    && mouseY > this.y && mouseY < this.y + this.h
    ) {
      return true;
    } 
    else return false;
  }
  
  public void action(){
    newGame(8 + 2 * this.id);
  }
}
class Card {
 float x;
 float y;
 float w;
 float h;
 int id;
 boolean visible;
 
 Card(float x, float y, float cardWidth, int id) {
   this.x = x;
   this.y = y;
   this.w = cardWidth;
   this.h = this.w * 3 / 4;
   this.visible = false;
   this.id = id;
   //this.numCards = numCards;
   //this.w = width / sqrt(2*(numCards + 2));
   //this.h = this.w;
   //this.h = width / (this.numCards + 2);
 }
 
 public void show() {
   if (this.visible){
   fill(255, 255, 255, 255);
   stroke(220);
   strokeWeight(this.w / 15);
   rect(this.x, this.y, this.w, this.h, this.w / 30);
   image(pics[this.id], this.x, this.y, this.w, this.h);
   } else {
     noStroke();
   fill(0, 100, 255, 255);
   rect(this.x, this.y, this.w, this.h, this.w / 30);
   image(cardBack, this.x, this.y, this.w, this.h);
   }
 }
}
  public void settings() {  size(800, 450); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Memory" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
