import neat
from flask import Flask, request, jsonify
import json

app = Flask(__name__)

current_generation = 0  # Generation counter

# Empty Collections For Nets and Cars
nets = []
inputs = []
outputs = []

# Chemin vers notre fichier JSON stockant les données
fichier_json = 'donnees.json'


def updateInputs():
    global inputs
    with open(fichier_json, 'r') as f:
        inputs = json.load(f)


def sendOutputs():
    try:
        response = json.dumps(inputs, ensure_ascii=False)
        return app.response_class(response, content_type='application/json')
    except Exception as e:
        print(f"Erreur : {e}")
        return jsonify({'error': str(e)}), 500


def run_simulation(genomes, config):
    # For All Genomes Passed Create A New Neural Network
    for i, g in genomes:
        net = neat.nn.FeedForwardNetwork.create(g, config)
        nets.append(net)
        g.fitness = 0

    # mettre à jour inputs

    global current_generation
    current_generation += 1

    # Simple Counter To Roughly Limit Time (Not Good Practice)
    counter = 0

    while True:

        # For Each Car Get The Acton It Takes
        for i, input in enumerate(inputs):
            output = nets[i].activate(input)
            choice = output.index(max(output))

            # envoyer à java choice

        # Check If Car Is Still Alive
        # Increase Fitness If Yes And Break Loop If Not
        still_alive = 0
        # for i, input in enumerate(inputs):
        #     if car.is_alive():
        #         still_alive += 1
        #         genomes[i][1].fitness += car.get_reward()

        if still_alive == 0:
            break

        counter += 1
        if counter == 30 * 40:  # Stop After About 20 Seconds
            break


if __name__ == "__main__":
    # # Load Config
    # config_path = "./config.txt"
    # config = neat.config.Config(neat.DefaultGenome,
    #                             neat.DefaultReproduction,
    #                             neat.DefaultSpeciesSet,
    #                             neat.DefaultStagnation,
    #                             config_path)
    #
    # # Create Population And Add Reporters
    # population = neat.Population(config)
    # population.add_reporter(neat.StdOutReporter(True))
    # stats = neat.StatisticsReporter()
    # population.add_reporter(stats)
    #
    # # Run Simulation For A Maximum of 1000 Generations
    # population.run(run_simulation, 1000)

    updateInputs()
    print(inputs)


