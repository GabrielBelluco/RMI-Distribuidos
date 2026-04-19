## ──────────────────────────────────────────────
##  RMI Clustering – Makefile
##  Java 8+  |  sem dependências externas
## ──────────────────────────────────────────────

SRC_DIR  = src
OUT_DIR  = out
SOURCES  = $(wildcard $(SRC_DIR)/*.java)
CLASSES  = $(patsubst $(SRC_DIR)/%.java,$(OUT_DIR)/%.class,$(SOURCES))

.PHONY: all compile server client clean

## Alvo padrão: compila todos os fontes
all: compile

compile: $(OUT_DIR)
	javac -d $(OUT_DIR) $(SOURCES)
	@echo "Compilação concluída. Classes em $(OUT_DIR)/"

$(OUT_DIR):
	mkdir -p $(OUT_DIR)

## Inicia o servidor RMI (porta padrão 1099)
server: compile
	java -cp $(OUT_DIR) DistanceServer

## Inicia o cliente RMI (conecta em localhost:1099)
client: compile
	java -cp $(OUT_DIR) DistanceClient

## Remove os arquivos compilados
clean:
	rm -rf $(OUT_DIR)
	@echo "Diretório $(OUT_DIR) removido."
