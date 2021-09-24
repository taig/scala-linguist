package io.taig.linguist

object Samples {
  val Cpp = """#include <iostream>
              |
              |int main() {
              |    std::cout << "Hello World!";
              |    return 0;
              |}""".stripMargin

  val Java = """class HelloWorld {
               |  static public void main( String args[] ) {
               |    System.out.println( "Hello World!" );
               |  }
               |}""".stripMargin

  val JavaScript = """console.log("Hello World");"""

  val Matlab =
    """% Hello World in MATLAB.
      |
      |disp('Hello World');""".stripMargin

  val ObjectiveC =
    """#import <Foundation/Foundation.h>
      |
      |int main(int argc, const char * argv[]) {
      |    @autoreleasepool {
      |        NSLog(@"Hello, World!");
      |    }
      |    return 0;
      |}""".stripMargin

  val Scala = """object HelloWorld extends App {
                |  println("Hello world!")
                |}""".stripMargin
}
