package edu.put.boardgamecollector151855

import android.content.Context
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory


class Parser {
    private var alert : AlertShower = AlertShower()

    fun parseGames(context : Context): Pair<MutableList<Game>,Int> {
        val list = mutableListOf<Game>()

        try{

                val xmlDoc : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    FileServicer.fileDownloadData
                )
                xmlDoc.documentElement.normalize()

                val items : NodeList = xmlDoc.getElementsByTagName("item")

            when (xmlDoc.getElementsByTagName("message").item(0)?.textContent) {
                "Invalid username specified" -> return Pair(list,0)
                " Your request for this collection has been accepted and will be processed. Please try again later for access. " -> return Pair(list,2)
                else -> {
                    for(i in 0 until items.length ){
                        val itemNode : Node = items.item(i)
                        if(itemNode.nodeType == Node.ELEMENT_NODE){
                            val elem =  itemNode as Element
                            val children = elem.childNodes

                            var id : String? = null
                            var title : String? = null
                            var year_pub : String? = null
                            var pic : String? = null

                            val tags = itemNode.attributes
                            for(j in 0 until tags.length){
                                val node = tags.item(j)
                                when (node.nodeName){
                                    "objectid" -> {id = node.nodeValue}
                                }
                            }
                            for (j in 0 until children.length){
                                val node = children.item(j)
                                if(node is Element){
                                    when(node.nodeName){
                                        "name" -> {title = node.textContent}
                                        "yearpublished" -> {year_pub = node.textContent}
                                        "thumbnail" -> {pic = node.textContent}
                                    }
                                }
                            }

                            var year : Int = year_pub?.toInt() ?: 1900
                            var picture : String = pic ?: "standard"
                            var idGame : Int = id?.toInt() ?: 0
                            var game = Game(idGame, title.toString(), year, picture)
                            list.add(game)
                        }
                    }
                }
            }
            }
            catch(ex : Exception)
            {
                println("An error occurred: ${ex.message} ")
                alert.CreateCommonAlert("There is an error. Please try again.", context, false)
            }
        return Pair(list,1)
    }

    fun parseGame(context : Context) : Pair<MutableList<String>,Int> {
        val list = mutableListOf<String>()
        try{
            val xmlDoc : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                FileServicer.fileDownloadData
            )
            xmlDoc.documentElement.normalize()
            var description = xmlDoc.getElementsByTagName("description").item(0)?.textContent
            var imageLink = xmlDoc.getElementsByTagName("image").item(0)?.textContent
            if(imageLink == null){
                imageLink="standard"
            }
            if (description == null ) {
                description = "No description"
            }
            list.add(description)
            list.add(imageLink)

            return Pair(list,1)
        }
        catch(ex : Exception){
            println("An error occurred: ${ex.message} ")
            alert.CreateCommonAlert("There is an error. Please try again.", context, false)
        }
        return Pair(list,0)
    }
}