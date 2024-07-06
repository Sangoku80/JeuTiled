from flask import Flask, request, jsonify
import json

app = Flask(__name__)


@app.route('/test', methods=['POST'])
def test():
    try:
        data = request.get_json()
        print(f"Reçu du client : {data}")
        response = json.dumps({'message': 'Entrées reçues avec succès !'}, ensure_ascii=False)
        return app.response_class(response, content_type='application/json')
    except Exception as e:
        print(f"Erreur : {e}")
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
