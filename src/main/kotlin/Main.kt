fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: [task code (check readme)] [filepath]")
    }
    println(Solution.run(args[0], args[1]).joinToString("\n"))
}