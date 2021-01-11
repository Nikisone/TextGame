package o1.adventure

import o1._
import scala.collection.mutable.Map
import scala.io.Source
import scala.collection.mutable.Buffer

/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false  // one-way flag
  private var items = Map[String, Item]()
  var isDead = false
  var gameWon = false
  var canDrive = false

  def walkthrough() = {
    val filename = "walkthrough.txt"
    for (line <- Source.fromFile(filename).getLines) {
      println(line)
    }
    ""
  }

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
  }


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = {
    "You rest for a while. Better get a move on, though."
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  def has(itemName: String): Boolean = items.contains(itemName)



  def drop(itemName: String) = {
    val removed = this.items.remove(itemName)
    for (oldItem <- removed) {
      location.addItem(oldItem)
    }

    if (removed.isDefined)
      "You drop the " + itemName + "."

    else
      "You don't have that"
  }

  def inventory = {
    if (items.nonEmpty) {
      "You are carrying: \n" + items.keys.mkString("\n")
    }
    else
      "You are empty-handed."
  }

  def examine(itemName: String) = {
    if (has(itemName)) {
      "You look closely at the " + itemName + ":\n" + items(itemName).description
    }
    else
      "If you want to examine something, you need to pick it up first."
  }

  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

  def get(itemName: String) = {
    val received = this.location.removeItem(itemName)
    for (newItem <- received) {
      this.items.put(newItem.name, newItem)
    }
    if (received.isDefined) "You pick up the " + itemName + "." else "There is no " + itemName + " here to pick up."
  }
  //Emme saaneet importattua oikeaaa kirjastoa kuvien näyttämiseen. o1 libraryn importtauksessa oli ongelmia.
  /*def viewMap = {
  val map = Pic("Bitchass.png")
  show(map.scaleBy(0.5))
  ""
  }*/

  def startDriving(): String = {
    if (items.contains("key") && items.contains("jerrycan") && location.name == "Two vehicles") {
      canDrive = true
      "You can now drive anywhere. Your turn count doesn't increase."
    }
    else
      "You can't drive yet. Find keys, gasoline and a car first!"
  }



  def help = "COMMANDS:\n\n" +
    "collect : Picks up the item of the given name, if possible.\n" +
    "drop : Drops the item of the given name, that is if you actually have the given item in your inventory.\n" +
    "inspect : Looks closely at the item and gives a description of it.\n" +
    "items : Checks out what you have found until now.\n" +
    "enter : Enters the given room or area.\n" +
    "rest : Doesn't do anything for the turn in question.\n" +
    "view : Opens up a picture of the hotel map.\n" +
    "quit : Exits the game.\n" +
    "startDriving : Starts driving, that is if you have the keys and jerrycan.\n" +
    "walkthrough : Gives more insight to the game. Also shows how to win."
}


