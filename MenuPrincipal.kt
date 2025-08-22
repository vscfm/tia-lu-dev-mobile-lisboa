
fun exibirMenuPrincipal(): Int {
    println("MENU PRINCIPAL")
    println("1. Gerenciar Itens")
    println("2. Criar Pedido")
    println("3. Consultar Pedidos")
    println("4. Sair")
    print("Escolha uma opção: ")

    return readln().toIntOrNull() ?: 0
}
