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
 
 void show() {
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
