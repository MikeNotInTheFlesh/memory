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
  
  void show(){
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
    textSize(this.h / 1.5);
    fill(255);
    text(8 + 2 * this.id, this.x + this.w / 2, this.y + this.h / 2.5);
    popStyle();
  }
  
  boolean checkPressed() {
    if (mouseX > this.x && mouseX < this.x+this.w
    && mouseY > this.y && mouseY < this.y + this.h
    ) {
      return true;
    } 
    else return false;
  }
  
  void action(){
    newGame(8 + 2 * this.id);
  }
}
