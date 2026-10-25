{
  "startPoint": {
    "x": 32.335711586901766,
    "y": 8.5183,
    "name": "",
    "locked": false,
    "headingDeg": 90
  },
  "lines": [
    {
      "id": "line-mvkaxvsi-ips3o2",
      "color": "#ffc516",
      "name": "preload",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 41.4872,
        "y": 14.4899
      },
      "controlPoints": [],
      "heading": {
        "type": "linear",
        "startDeg": 90,
        "endDeg": 56,
        "reverse": false
      }
    },
    {
      "id": "line-mvluajdt-qkwxj2",
      "color": "#9C5BD5",
      "name": "flower",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 47.179471032745596,
        "y": 132.4782115869018
      },
      "controlPoints": [
        {
          "x": 12.873425692695221,
          "y": 78.8413098236776
        }
      ],
      "heading": {
        "type": "linear",
        "reverse": false,
        "startDeg": 56,
        "endDeg": 90
      }
    },
    {
      "id": "line-mvluk8fd-myocnf",
      "color": "#87D8BA",
      "name": "flowerBackup",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 43.4954,
        "y": 123.2726
      },
      "controlPoints": [
        {
          "x": 37.75251889168766,
          "y": 130.7405541561713
        }
      ],
      "heading": {
        "type": "linear",
        "reverse": true,
        "startDeg": 90,
        "endDeg": 381,
        "degrees": 90
      }
    },
    {
      "id": "line-mvlumrx3-9rhgyq",
      "color": "#A7A977",
      "name": "flowerShoot",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 43.5,
        "y": 123.2726
      },
      "controlPoints": [],
      "heading": {
        "type": "constant",
        "reverse": true,
        "degrees": 300
      }
    },
    {
      "id": "line-mvluq53r-kqs841",
      "color": "#7ADCAB",
      "name": "flower2",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 11.64831763224181,
        "y": 46.976934760705255
      },
      "controlPoints": [
        {
          "x": 20.63602015113351,
          "y": 88.70340050377834
        },
        {
          "x": 31.406801007556677,
          "y": 26.516372795969776
        }
      ],
      "heading": {
        "type": "linear",
        "reverse": true,
        "startDeg": 300,
        "endDeg": 180
      }
    },
    {
      "id": "line-mvmut6vc-t9difc",
      "color": "#7AD869",
      "name": "flower2shot",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 44.8633501259446,
        "y": 26.84193954659951
      },
      "controlPoints": [
        {
          "x": 24.22858942065491,
          "y": 30.64483627204031
        }
      ],
      "heading": {
        "type": "linear",
        "reverse": true,
        "startDeg": 180,
        "endDeg": 56
      }
    },
    {
      "id": "line-mvmuwvpt-epakh9",
      "color": "#95A567",
      "name": "park",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "kind": "atomic",
      "endPoint": {
        "x": 15.486146095717883,
        "y": 90.87972292191435
      },
      "controlPoints": [
        {
          "x": 9.913727959697743,
          "y": 62.53589420654913
        }
      ],
      "heading": {
        "type": "linear",
        "reverse": true,
        "startDeg": 56,
        "endDeg": 0
      }
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        {
          "x": 141.5,
          "y": 70
        },
        {
          "x": 141.5,
          "y": 141.5
        },
        {
          "x": 118.3,
          "y": 141.5
        },
        {
          "x": 135.5,
          "y": 118
        },
        {
          "x": 136.3,
          "y": 70.2
        }
      ],
      "color": "#dc2626",
      "fillColor": "#ff6b6b"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        {
          "x": 6.2,
          "y": 116.9
        },
        {
          "x": 25,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 70
        },
        {
          "x": 6,
          "y": 70
        }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "line-mvkaxvsi-ips3o2"
    },
    {
      "kind": "path",
      "lineId": "line-mvluajdt-qkwxj2"
    },
    {
      "kind": "path",
      "lineId": "line-mvluk8fd-myocnf"
    },
    {
      "kind": "path",
      "lineId": "line-mvlumrx3-9rhgyq"
    },
    {
      "kind": "path",
      "lineId": "line-mvluq53r-kqs841"
    },
    {
      "kind": "path",
      "lineId": "line-mvmut6vc-t9difc"
    },
    {
      "kind": "path",
      "lineId": "line-mvmuwvpt-epakh9"
    }
  ],
  "fieldPoints": [],
  "version": "1.5.0",
  "timestamp": "2026-10-25T00:19:51.188Z"
}