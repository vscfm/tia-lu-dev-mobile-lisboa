fun main() {
    // LISTAS DE DADOS
    val itens = mutableListOf<MutableMap<String, Any>>() // cada item é um mapa com código, nome, descrição, preço e quantidade
    // por que não utilizou o data class, se eu tinha falado que poderia usar? facilitava o código nesse momento.

    val pedidos = mutableListOf<MutableMap<String, Any>>() // cada pedido é um mapa com item, quantidade, desconto e status
    // mesma coisa aqui, não deveria ter sido um data class?

    var codigoAtual = 1 // essa variável é para o gerenciamento do código do item


    do {
        // MENU PRINCIPAL
        println("\nFOODDELIVERY ")
        println("1. Cadastrar Item")
        println("2. Atualizar Item")
        println("3. Criar Pedido")
        println("4. Atualizar Pedido")
        println("5. Consultar Pedidos")
        println("0. Sair")
        print("Escolha uma opção: ")

        // esse valor lido deveria ser armazenado numa variável de controle e não ir direto para o when.
        when (readln()) {
            //CADASTRAR ITEM
            "1" -> {
                print("Nome do item: ")
                val nome = readln() // seria interessante ter padronizado o nome do item, tipo: tudo maiusculo, ou tudo minusculo.
                print("Descrição do item: ")
                val descricao = readln()
                print("Preço (ex: 10.50): ")
                val preco = readln().replace(',', '.').toDoubleOrNull() ?: 0.0 // nesse caso, caso não seja informado nada o valor padrão do preco é zero.
                print("Quantidade em estoque: ")
                val quantidade = readln().toIntOrNull() ?: 0 // mesma coisa aqui, o valor padrão da quantidade é zero.

                // dúvidas: e se o preço for negativo, vamos permitir armazenar?
                // se a quantidade for negativa também?
                // o nome pode estar vazio?

                itens.add(
                    mutableMapOf(
                        "codigo" to codigoAtual,
                        "nome" to nome,
                        "descricao" to descricao,
                        "preco" to preco,
                        "quantidade" to quantidade
                    )
                )
                println("Item cadastrado! Código: $codigoAtual")
                codigoAtual++
            }

            //ATUALIZAR ITEM
            "2" -> when { // aqui cabia uma indentação melhor
                itens.isEmpty() -> println("Nenhum item cadastrado.")
                else -> {
                    println("Itens cadastrados:")
                    // talvez valia você ter imprimido primeiro o cabeçalo e depois os valores. imprimir como se fosse uma tabela mesmo.
                    itens.forEach { item ->
                        println("Código ${item["codigo"]} - ${item["nome"]}: ${item["descricao"]} (R$ ${"%.2f".format(item["preco"])}, ${item["quantidade"]} unid)")
                    }
                    print("Digite o código do item para atualizar: ")
                    val cod = readln().toIntOrNull() // se o cod for nulo, faz sentido buscar?
                    val item = itens.find { it["codigo"] == cod } // esse uso do find, o que refere-se a variável it?
                    when {
                        item == null -> println("Código inválido!") // talvez fosse melhor colocar produto não encontrado também.
                        else -> {
                            print("Novo nome (${item["nome"]}): ")
                            val n = readln() // nome significativo para variável: novoNome
                            if (n.isNotBlank()) item["nome"] = n // esse tipo de verificação também deveria ter no cadastro.

                            print("Nova descrição (${item["descricao"]}): ")
                            val d = readln() // nome significativo: novaDescricao
                            if (d.isNotBlank()) item["descricao"] = d

                            print("Novo preço (${item["preco"]}): ")
                            val p = readln()
                            if (p.isNotBlank()) item["preco"] = p.replace(',', '.').toDouble()

                            print("Nova quantidade (${item["quantidade"]}): ")
                            val q = readln()
                            if (q.isNotBlank()) item["quantidade"] = q.toInt()

                            println("Item atualizado!")
                        }
                    }
                }
            }

            //CRIAR PEDIDO
            "3" -> when {
                itens.isEmpty() -> println("Nenhum item disponível.")
                else -> {
                    println("Itens disponíveis:")
                    itens.forEach { item ->
                        println("Código ${item["codigo"]} - ${item["nome"]}: ${item["descricao"]} (R$ ${"%.2f".format(item["preco"])}, ${item["quantidade"]} unid)")
                    }

                    //o pedido tem que ter o mínimo de um item, mas não quer dizer que apenas 1 item, ele pode adicionar quantos itens quiser
                    // até que encare que tem todos os itens
                    print("Digite o código do item para pedir: ")
                    val cod = readln().toIntOrNull()
                    val item = itens.find { it["codigo"] == cod } // se o valor de cod for nulo, o resultado do item sempre será nulo

                    when {
                        // ai o usuario volta para o menu principal? não deveria dar a opção dele escolher outro item?
                        item == null || (item["quantidade"] as Int) <= 0 -> println("Código inválido ou sem estoque!")

                        else -> {
                            print("Quantidade: ")
                            val qtd = readln().toIntOrNull() ?: 0 // valor padrão para o valor vazio é zero
                            when {
                                qtd <= 0 -> println("Quantidade inválida!") // tá, mas ai a pessoa não tem como digitar novamente?
                                qtd > (item["quantidade"] as Int) -> println("Estoque insuficiente!")
                                else -> {
                                    print("Possui cupom de desconto? (S/N): ")
                                    val cupom = readln()
                                    val desconto = if (cupom.equals("S", true)) 0.1 else 0.0 // 10% desconto

                                    val precoTotal = (item["preco"] as Double) * qtd //precoTotal nesse caso seria o totalPedido
                                    val precoFinal = precoTotal - (precoTotal * desconto) // totalPedidoComDesconto

                                    // aqui era legal ter criado um código único para cada pedido também, ajudava na busca ou alteração futuramente.
                                    pedidos.add(
                                        mutableMapOf(
                                            "item" to item,
                                            "quantidade" to qtd,
                                            "status" to "ACEITO",
                                            "precoFinal" to precoFinal
                                        )
                                    )
                                    item["quantidade"] = (item["quantidade"] as Int) - qtd

                                    //isso aqui era para ter acontecido depois do usuário poder escolher quantos itens quisesse
                                    println("Pedido criado! Valor final: R$ ${"%.2f".format(precoFinal)}")
                                }
                            }
                        }
                    }
                }
            }

            //ATUALIZAR PEDIDO
            "4" -> when {
                pedidos.isEmpty() -> println("Nenhum pedido registrado.")
                else -> {
                    println("Pedidos:")
                    pedidos.forEachIndexed { index, pedido ->
                        // não entendi porque o pedido tá sendo convertido para um map nesse caso.
                        println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - ${pedido["quantidade"]} unid (Status: ${pedido["status"]})")
                    }
                    print("Número do pedido para atualizar: ")
                    val i = readln().toIntOrNull()?.minus(1)// se ele não informar um int, vai retornar null e não executa o minus
                    when {
                        // condição interessante
                        i == null || i !in pedidos.indices -> println("Pedido não encontrado!")
                        else -> {
                            println("Escolha novo status: ")
                            println("1 - FAZENDO\n2 - FEITO\n3 - ESPERANDO ENTREGADOR\n4 - SAIU PARA ENTREGA\n5 - ENTREGUE")
                            when (readln()) {
                                "1" -> pedidos[i]["status"] = "FAZENDO"
                                "2" -> pedidos[i]["status"] = "FEITO"
                                "3" -> pedidos[i]["status"] = "ESPERANDO ENTREGADOR"
                                "4" -> pedidos[i]["status"] = "SAIU PARA ENTREGA"
                                "5" -> pedidos[i]["status"] = "ENTREGUE"
                                else -> println("Status inválido!")
                            }
                            println("Status atualizado!") // como você pode imprimir que o status foi atualizado
                            // se ele pode informar uma opção inválida
                        }
                    }
                }
            }

            // CONSULTAR PEDIDOS
            "5" -> when {
                pedidos.isEmpty() -> println("Nenhum pedido registrado.")
                else -> {
                    println("CONSULTAR PEDIDOS")
                    //aqui teria sido legal ter um enum, pois conforme a gente aumentasse os status, o código ficaria mais fácil de se adaptar
                    println("1 - Todos\n2 - ACEITO\n3 - FAZENDO\n4 - FEITO\n5 - ESPERANDO ENTREGADOR\n6 - SAIU PARA ENTREGA\n7 - ENTREGUE")
                    print("Escolha uma opção: ")
                    val opc = readln()
                    //outro ponto que aqui o item teria sido a comparação com o status mesmo. O usuário poderia fazer duas coisas aqui:
                    /***
                     * mas uma vez que vocês fizeram assim, o melhor mesmo era ter:
                     * 1. Um when que converte o valor numérico em status
                     * val generalStatus = when(opc) {
                     *                         "1" -> "TODOS"
                     *                         "2" -> "ACEITO"
                     *                         "3" -> "FAZENDO"
                     *                         "4" -> "FEITO"
                     *                         "5" -> "ESPERANDO ENTREGADOR"
                     *                         "6" -> "SAIU PARA ENTREGA"
                     *                         "7" -> "ENTREGUE"
                     *                         else -> "DESCONHECIDO"
                     *                     }
                     * 2. o filtro seria
                     *   val filteredOrdersByStatus: List<MutableMap<String, Any>> = when(generalStatus) {
                     *                         "TODOS" -> {
                     *                             pedidos
                     *                         }
                     *                         "ACEITO", "FAZENDO", "FEITO", "ESPERANDO ENTREGADOR", "SAIU PARA ENTREGA", "ENTREGUE" -> {
                     *                             pedidos.filter { pedido -> pedido["status"] == generalStatus }
                     *                         }
                     *                         else -> mutableListOf()
                     *                     }
                     *
                     * 3. impressão dos valores:
                     *       filteredOrdersByStatus.forEachIndexed { index, pedido ->
                     *                         val item = pedido["item"] as? MutableMap<String, Any>
                     *                         println("${index + 1} - ${item?.get("nome")} - ${pedido["status"]}")
                     *                     }
                     */

                    pedidos.forEachIndexed { index, pedido ->
                        val status = pedido["status"]
                        when (opc) {
                            "1" -> println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} (${pedido["quantidade"]} unid) - Status: $status")
                            "2" -> if (status == "ACEITO") println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - $status")
                            "3" -> if (status == "FAZENDO") println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - $status")
                            "4" -> if (status == "FEITO") println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - $status")
                            "5" -> if (status == "ESPERANDO ENTREGADOR") println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - $status")
                            "6" -> if (status == "SAIU PARA ENTREGA") println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - $status")
                            "7" -> if (status == "ENTREGUE") println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - $status")
                            // e se for qualquer outra opção, não informamos que a opção foi inválida e pedimos para ele informar novamente?
                        }
                    }
                }
            }

            //SAIR
            "0" -> println("Encerrando o sistema...") // no final ele nunca sai, pois o while é true

            //OPÇÃO INVÁLIDA
            else -> println("Opção inválida!")
        }
    } while (true) // não é porque você usou o do while que tá certo. Se você não usar o do..while com a condição de parada, não vai tá correto.
    // esse código roda indefinidamente, mesmo que o usuário escolha a opção de sair, o programa nunca termina.
}