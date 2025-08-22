
fun main() {
    while (true) {
        when (exibirMenuPrincipal()) {
            1 -> gerenciarItens()
            2 -> criarPedido()
            3 -> consultarPedidos()
            4 -> {
                println("👋 Saindo...")
                break
            }
            else -> println("Opção inválida!")
        }
    }
}
