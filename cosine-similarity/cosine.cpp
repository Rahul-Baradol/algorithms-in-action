#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <sstream>
#include <algorithm>
#include <cmath>

using namespace std;

class Store {
private:  
    vector<pair<string, vector<int> > > sentence_and_its_vector;

    double dot_product(const vector<int> a, const vector<int> b) {
        if (a.size() != b.size()) {
            cerr << "Vectors must be of the same size!" << endl;
            return -1;
        }

        double result = 0;
        for (size_t i = 0; i < a.size(); ++i) {
            result += (double) a[i] * b[i] * 1.0;
        }
        return result;
    }

    double magnitude(const vector<int> v) {
        double sum = 0;
        for (const auto& val : v) {
            sum += (double) val * val * 1.0;
        }
        return sqrt(sum);
    }

    double cosine(const vector<int> a, const vector<int> b) {
        if (a.size() != b.size()) {
            cerr << "Vectors must be of the same size!" << endl;
            return -1;
        }

        double result = (double) dot_product(a, b) * 1.0 / (magnitude(a) * magnitude(b)) * 1.0;
        return result;
    }

public:
    void add_vector(string sentence, vector<int> sentence_vector) {
        sentence_and_its_vector.push_back(make_pair(sentence, sentence_vector));
    }

    vector<pair<double, string> > search(vector<int> query_vector) {
        vector<pair<double, string> > result;

        for (auto element: sentence_and_its_vector) {
            string sentence = element.first;
            vector<int> vector = element.second;

            double similarity = cosine(vector, query_vector);
            result.push_back(make_pair(similarity, sentence));
        }

        sort(result.rbegin(), result.rend());
        return result;
    }   
};

vector<string> split(string &sentence) {
    stringstream stream(sentence);
    string word;

    vector<string> words;
    while (stream >> word) {
        words.push_back(word);
    }

    return words;
}

void populate(vector<string> &sentences) {
    sentences.push_back("life is a journey full of unexpected turns");
    sentences.push_back("the essence of life lies in its unpredictability");
    sentences.push_back("life is an endless quest for meaning");
    sentences.push_back("living in the moment brings peace and joy");
    sentences.push_back("life teaches us lessons through challenges");
    sentences.push_back("the beauty of life is in its simple moments");
    sentences.push_back("lifes greatest gift is the ability to grow");
    sentences.push_back("every experience in life shapes who we become");
    sentences.push_back("life is an adventure waiting to unfold");
    sentences.push_back("the meaning of life is different for everyone");

    sentences.push_back("love is the most powerful force in the universe");
    sentences.push_back("true love is about understanding and trust");
    sentences.push_back("love makes life worth living");
    sentences.push_back("in love we find both strength and vulnerability");
    sentences.push_back("love is a journey not a destination");
    sentences.push_back("unconditional love transcends time and distance");
    sentences.push_back("love has the power to heal and to hurt");
    sentences.push_back("love is the foundation of all human connections");
    sentences.push_back("to love and be loved is to feel alive");
    sentences.push_back("love grows deeper through shared experiences");

    sentences.push_back("romance is the art of expressing love");
    sentences.push_back("a touch of romance brings magic to relationships");
    sentences.push_back("romance thrives in the little gestures of affection");
    sentences.push_back("romantic moments are cherished memories for a lifetime");
    sentences.push_back("romance is the spark that ignites deep emotions");
    sentences.push_back("a romantic sunset can take your breath away");
    sentences.push_back("romance flourishes when love and passion meet");
    sentences.push_back("the essence of romance lies in shared moments");
    sentences.push_back("romance makes ordinary moments feel extraordinary");
    sentences.push_back("true romance is timeless and knows no bounds");

    sentences.push_back("being a nerd is about embracing your passions unapologetically");
    sentences.push_back("nerds are the ones who change the world with their ideas");
    sentences.push_back("a nerds love for knowledge knows no bounds");
    sentences.push_back("nerds dive deep into the things they love from books to technology");
    sentences.push_back("in the age of technology being a nerd is a superpower");
    sentences.push_back("nerd culture celebrates curiosity and innovation");
    sentences.push_back("nerds find joy in solving complex problems");
    sentences.push_back("from comics to coding nerds explore every corner of their interests");
    sentences.push_back("nerds arent afraid to be passionate about what they love");
    sentences.push_back("the nerd revolution is shaping the future of our world");
}

int main() {
    vector<string> sentences;
    populate(sentences);    

    set<string> vocabolary;

    for (string sentence: sentences) {
        vector<string> tmp = split(sentence);
        for (string word: tmp) {
            vocabolary.insert(word);
        }
    }

    int vocabolary_size = vocabolary.size();

    map<string, int> word_to_index;

    int index = 0;
    for (string ele: vocabolary) {
        word_to_index[ele] = index++;
    }

    Store *store = new Store();
    for (string sentence: sentences) {
        vector<string> tokens = split(sentence);
        vector<int> sentence_vector(vocabolary_size, 0);
        for (string token: tokens) {
            sentence_vector[word_to_index[token]]++;
        }

        store -> add_vector(sentence, sentence_vector);
    }

    int n;
    cin >>  n;

    vector<string> query_v;
    for (int i = 0; i <  n; i++) {
        string tmp;
        cin >> tmp;
        query_v.push_back(tmp);
    }

    vector<string> tokens = query_v;
    vector<int> query_vector(vocabolary_size, 0);
    for (string token: tokens) {
        query_vector[word_to_index[token]]++;
    }

    vector<pair<double, string> > result = store -> search(query_vector);
    for (int i = 0; i < result.size(); i++) {
        cout << result[i].first << " " << result[i].second << "\n";
    }
    
    return 0;
}
