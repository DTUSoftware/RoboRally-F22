package dk.dtu.compute.se.pisd.roborally_server.model;

import org.json.JSONObject;
import java.util.List;

/* Don't use Map, reminded myself that why would I do this, and not just send the JSON file, lol... */

/***
 * @author Marcus S. (s215827)
 */
public class Map {
    Size size;
    List<Field> board;

    class Size {
        int width;
        int height;

        Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    class Field {
        class Position {
            int x;
            int y;

            Position(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        class Element {
            String type;
            String direction;
            boolean color;
            int number;
            Registers registers;
            Bounds bounds;

            class Bounds {
                int x1;
                int y1;
                int x2;
                int y2;

                Bounds(int x1, int y1, int x2, int y2) {
                    this.x1 = x1;
                    this.y1 = y1;
                    this.x2 = x2;
                    this.y2 = y2;
                }
            }

            class Registers {
                int register1;
                int register2;

                Registers(int register1, int register2) {
                    this.register1 = register1;
                    this.register2 = register2;
                }
            }

            Element(String type) {
                this.type = type;
            }

            void setDirection(String direction) {
                this.direction = direction;
            }

            void setColor(boolean color) {
                this.color = color;
            }

            void setNumber(int number) {
                this.number = number;
            }

            void setRegisters(int register1, int register2) {
                this.registers = new Registers(register1, register2);
            }

            void setBounds(int x1, int y1, int x2, int y2) {
                this.bounds = new Bounds(x1, y1, x2, y2);
            }
        }
    }

    public Map() {}

    public Map(JSONObject map) {
        super();
        JSONObject sizeJSON = map.getJSONObject("size");
        this.size = new Size(sizeJSON.getInt("width"), sizeJSON.getInt("height"));

        JSONObject boardJSON = map.getJSONObject("board");

    }
}
