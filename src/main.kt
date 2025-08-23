fun main() {
    // LISTAS DE DADOS
    val itens = mutableListOf<MutableMap<String, Any>>() // cada item é um mapa com código, nome, descrição, preço e quantidade
    val pedidos = mutableListOf<MutableMap<String, Any>>() // cada pedido é um mapa com item, quantidade, desconto e status
    var codigoAtual = 1

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

        when (readln()) {
            //CADASTRAR ITEM
            "1" -> {
                print("Nome do item: ")
                val nome = readln()
                print("Descrição do item: ")
                val descricao = readln()
                print("Preço (ex: 10.50): ")
                val preco = readln().replace(',', '.').toDoubleOrNull() ?: 0.0
                print("Quantidade em estoque: ")
                val quantidade = readln().toIntOrNull() ?: 0

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
            "2" -> when {
                itens.isEmpty() -> println("Nenhum item cadastrado.")
                else -> {
                    println("Itens cadastrados:")
                    itens.forEach { item ->
                        println("Código ${item["codigo"]} - ${item["nome"]}: ${item["descricao"]} (R$ ${"%.2f".format(item["preco"])}, ${item["quantidade"]} unid)")
                    }
                    print("Digite o código do item para atualizar: ")
                    val cod = readln().toIntOrNull()
                    val item = itens.find { it["codigo"] == cod }
                    when {
                        item == null -> println("Código inválido!")
                        else -> {
                            print("Novo nome (${item["nome"]}): ")
                            val n = readln()
                            if (n.isNotBlank()) item["nome"] = n

                            print("Nova descrição (${item["descricao"]}): ")
                            val d = readln()
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
                    print("Digite o código do item para pedir: ")
                    val cod = readln().toIntOrNull()
                    val item = itens.find { it["codigo"] == cod }

                    when {
                        item == null || (item["quantidade"] as Int) <= 0 -> println("Código inválido ou sem estoque!")
                        else -> {
                            print("Quantidade: ")
                            val qtd = readln().toIntOrNull() ?: 0
                            when {
                                qtd <= 0 -> println("Quantidade inválida!")
                                qtd > (item["quantidade"] as Int) -> println("Estoque insuficiente!")
                                else -> {
                                    print("Possui cupom de desconto? (S/N): ")
                                    val cupom = readln()
                                    val desconto = if (cupom.equals("S", true)) 0.1 else 0.0 // 10% desconto

                                    val precoTotal = (item["preco"] as Double) * qtd
                                    val precoFinal = precoTotal - (precoTotal * desconto)

                                    pedidos.add(
                                        mutableMapOf(
                                            "item" to item,
                                            "quantidade" to qtd,
                                            "status" to "ACEITO",
                                            "precoFinal" to precoFinal
                                        )
                                    )
                                    item["quantidade"] = (item["quantidade"] as Int) - qtd
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
                        println("${index + 1} - ${(pedido["item"] as MutableMap<String, Any>)["nome"]} - ${pedido["quantidade"]} unid (Status: ${pedido["status"]})")
                    }
                    print("Número do pedido para atualizar: ")
                    val i = readln().toIntOrNull()?.minus(1)
                    when {
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
                            println("Status atualizado!")
                        }
                    }
                }
            }

            // CONSULTAR PEDIDOS
            "5" -> when {
                pedidos.isEmpty() -> println("Nenhum pedido registrado.")
                else -> {
                    println("CONSULTAR PEDIDOS")
                    println("1 - Todos\n2 - ACEITO\n3 - FAZENDO\n4 - FEITO\n5 - ESPERANDO ENTREGADOR\n6 - SAIU PARA ENTREGA\n7 - ENTREGUE")
                    print("Escolha uma opção: ")
                    val opc = readln()
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
                        }
                    }
                }
            }

            //SAIR
            "0" -> println("Encerrando o sistema...")

            //OPÇÃO INVÁLIDA
            else -> println("Opção inválida!")
        }
    } while (true)
}
