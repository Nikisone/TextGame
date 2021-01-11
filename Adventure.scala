package o1.adventure


/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "Slender Man"

  val rustedTankers         = new Area("Rusted oil tanks", "You are somewhere in the woods. There are a lot of old oil tanks here.\nA fox has made a nest inside one of the tanks.")
  private val tunnel        = new Area("A tunnel", "You are somewhere in the woods. A mountain blocks your way further north.\nYou can hear the rain hitting the metal roof.")
  private val silo          = new Area("An old silo", "A river blocks your way further south or east.")
  private val cuttedWoods   = new Area("Cutted woods.", "They are somewhat freshly cutted!")
  private val bathrooms     = new Area("A bathroom building", "You are in the middle of the forest.\nThere are trails in every direction.")
  private val crossWalls    = new Area("A cross made of two brick walls.", "You are in a dense forest.\n Only exit is to go back.")
  private val largeRocks    = new Area("Two large rocks", "You can't go further east!")
  private val twoVehicles   = new Area("Two vehicles", "You need keys and gasoline to start up the cars!\nThe vehicles are blocking your way further west to a cave.")
  private val scaryTree     = new Area("A large rotten tree", "You can see birds skeleton hanging from one of its branches.\nA river is blocking your path to South.")
  private val singleTanker  = new Area("A single tanker", "The forest is densing heavily.\nOnly exits are to South and East.")
  private val waypoint      = new Area("You are in a little clearing in the forest.", "You should keep going!")
  private val end           = new Area("There's a dark forest infront of you!", "Available paths go West, North and East.")
  private val destination   = end

      rustedTankers.setNeighbors(Vector("north" -> tunnel, "east" -> largeRocks, "south" -> end, "west" -> bathrooms   ))
      tunnel.setNeighbors(Vector(                        "east" -> cuttedWoods, "south" -> rustedTankers,      "west" -> singleTanker   ))
      silo.setNeighbors(Vector("east" -> largeRocks,   "west" -> end   ))
      cuttedWoods.setNeighbors(Vector("east" -> waypoint))
      crossWalls.setNeighbors(Vector("west" -> waypoint))
      largeRocks.setNeighbors(Vector("north" -> waypoint, "west" -> rustedTankers, "south" -> silo     ))
      singleTanker.setNeighbors(Vector("east" -> tunnel, "south" -> twoVehicles    ))
      twoVehicles.setNeighbors(Vector("north" -> singleTanker, "east" -> bathrooms, "south" -> scaryTree))
      scaryTree.setNeighbors(Vector("east" -> end, "west" -> twoVehicles))
      bathrooms.setNeighbors(Vector("east" -> rustedTankers, "west" -> twoVehicles))
      waypoint.setNeighbors(Vector("north" -> tunnel, "east" -> crossWalls, "south" -> largeRocks, "west" -> cuttedWoods))
      end.setNeighbors(Vector("north" -> rustedTankers, "east" -> silo, "west" -> scaryTree     ))


  end.addItem(new Item("page 1", "You need 8 hints to get out!"))
  silo.addItem(new Item("page 2", "You can't run from me!!!"))
  rustedTankers.addItem(new Item("page 3", "Always watches, no eyes."))
  singleTanker.addItem(new Item("page 4", "HELP ME!"))
  largeRocks.addItem(new Item("page 5", "Don't look... or it takes YOU!"))
  bathrooms.addItem(new Item("page 6", "Leave me alone."))
  crossWalls.addItem(new Item("page 7", "FOLLOWS..."))
  scaryTree.addItem(new Item("page 8", "Don't look back!!!"))
  largeRocks.addItem(new Item("flashlight", "You founf a flashlight but there's not batteries."))
  singleTanker.addItem(new Item("jerrycan", "You picked up a jerrycan. You can fill up the cars tank with this!"))
  cuttedWoods.addItem(new Item("car keys", "You found car keys from the ground. You might be able to start up one of the cars!"))
  end.addItem(new Item("matches", "Someone has left matches to the tunnel. But be aware: Don't drop them near oil tanks."))



  /** The character that the player controls in the game. */
  val player = new Player(end)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 42


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination && player.has("page 1") && player.has("page 2") && player.has("page 3") && player.has("page 4") && player.has("page 5") && player.has("page 6") && player.has("page 7") && player.has("page 8")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || this.rustedTankers.isOnFire

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You heard a scream from the forest. While you are walking towards the entrance your vision starts to decay. For your luck it stops as fast as it came.\nNow you feel like someone is following you..."


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "You survived the forest!\n You won the game"
    else if (this.turnCount == this.timeLimit)
      "Slenderman captured you! There's no going back.\nYou lost the game.!"
    else if (rustedTankers.isOnFire)
      "There were still oil in the tanks. You blew up.\nBe carefull with flammable items!\nYou lost the game."

    else  // game over due to player quitting
      "Quitter!"
  }


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined && !player.canDrive) {
      this.turnCount += 1
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }


}

